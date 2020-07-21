package com.example.debitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PhotoCopyAdapter.OnClick {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager manager;
    PhotoCopyAdapter adapter;
    CheckBox cBphotocopy,cBprinnt,cBtransfer;
    Spinner SPhotocopy,SPrint,Stransfer;
    String[] photocopy = {"", "single", "both", "A3"};
    String[] print={"","Black and White","Colour"};
    String[] transfer={"","Mobile Transfer","Internet Bills","TV Bills","Others"};
    String custName;
    StringBuffer buffer=new StringBuffer();
    EditText etPC1,etPC2,etPC3,etPR1,etPR2,etTR1,etTR2,etTR3,etTR4,etName,etPaidAmt;
    EditText cost1,cost2,cost3,cost4,cost5;
    Button btPC1,btPC2,btPC3,calculate,btPR1,btPR2,btTR1,btTR2,btTR3,btTR4,update_db,Paybtn;
    double qn1,qn2,qn3,qn4,qn5=0;
    double c1,c2,c3,c4,c5,c6,c7,c8,c9=0;
    double total,total_photocopy,total_print,total_transfer;
    double current_price,currph,currpr,currtr,paid_amt,current_price1,currph1,currpr1,currtr1;
    double new_price,newph,newpr,newtr;
    MyDataBaseHelper mydb;
    AlertDialog.Builder builder,builder1,priceBuilder;
    AlertDialog dialog,pricedialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Photocopy");


        recyclerView=findViewById(R.id.reV);
        manager=new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        adapter=new PhotoCopyAdapter(this, ApplicationClass.photcopyList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickUpdate(final int index) {
        mydb=new MyDataBaseHelper(getApplicationContext());
        qn1=qn2=qn3=qn4=qn5=0;
        c1=c2=c3=c4=c5=c6=c7=c8=c9;
        showView();
        update_db.setVisibility(View.VISIBLE);
        calculate.setVisibility(View.GONE);
        etName.setText(ApplicationClass.photcopyList.get(index).getName());
        currph=ApplicationClass.photcopyList.get(index).getTotal_photocopy();
        currpr=ApplicationClass.photcopyList.get(index).getTotal_print();
        currtr=ApplicationClass.photcopyList.get(index).getTotal_tranfer();
        current_price=ApplicationClass.photcopyList.get(index).getPrice();
        custName=ApplicationClass.photcopyList.get(index).getName();
        update_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newph=currph+qn1+qn2+qn3;
                newpr=currpr+qn4+qn5;
                newtr=currtr+c6+c7+c8+c9;
                new_price=current_price+(qn1*c1)+(qn2*c2)+(qn3*c3)+(qn4*c4)+(qn5*c5)+c6+c7+c8+c9;
                if(new_price==current_price)
                {
                    Toast.makeText(MainActivity.this,"PLease enter the new values",Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean is_updated=mydb.upgrade(custName,newph,newpr,newtr,new_price);
                    if(is_updated)
                    {
                        Toast.makeText(MainActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                        ApplicationClass.photcopyList.remove(index);
                        adapter.notifyDataSetChanged();
                        ApplicationClass.photcopyList.add(index,new Photcopy(custName,newph,newpr,newtr,new_price));
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Not Updated",Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });




    }

    @Override
    public void onClickPayment(final int index) {
        mydb=new MyDataBaseHelper(getApplicationContext());
        current_price1=ApplicationClass.photcopyList.get(index).getPrice();
        custName=ApplicationClass.photcopyList.get(index).getName();
        currph1=ApplicationClass.photcopyList.get(index).getTotal_photocopy();
        currpr1=ApplicationClass.photcopyList.get(index).getTotal_print();
        currtr1=ApplicationClass.photcopyList.get(index).getTotal_tranfer();
        builder1=new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to pay all the due amount");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int isdeleted=mydb.delete(custName);
                if(isdeleted>0)
                {
                    Toast.makeText(MainActivity.this,"Paid",Toast.LENGTH_SHORT).show();
                    ApplicationClass.photcopyList.remove(index);
                    adapter.notifyDataSetChanged();

                }
                else
                {
                    Toast.makeText(MainActivity.this,"Not Paid",Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                dialog.cancel();
                priceBuilder=new AlertDialog.Builder(MainActivity.this);
                View view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.update_record,null);
                etPaidAmt=view.findViewById(R.id.eTpaidAmt);
                Paybtn=view.findViewById(R.id.Clearbtn);
                priceBuilder.setView(view);
                priceBuilder.setCancelable(true);
                pricedialog=priceBuilder.create();
                pricedialog.show();

                Paybtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       paid_amt=Double.parseDouble(etPaidAmt.getText().toString());
                       new_price=current_price1-paid_amt;
                       boolean isDecreased=mydb.upgrade(custName,currph,currpr,currtr,new_price);
                       if(isDecreased)
                       {
                           Toast.makeText(MainActivity.this,paid_amt+" is paid",Toast.LENGTH_SHORT).show();
                           ApplicationClass.photcopyList.remove(index);
                           adapter.notifyDataSetChanged();
                           ApplicationClass.photcopyList.add(index,new Photcopy(custName,currph,currpr,currtr,new_price));
                           adapter.notifyDataSetChanged();
                           pricedialog.dismiss();

                       }

                   }
               });



            }
        });

        builder1.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add: {
                qn1=qn2=qn3=qn4=qn5=0;
                c1=c2=c3=c4=c5=c6=c7=c8=c9=0;
                showView();
            }
            default: {
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showView() {
        builder = new AlertDialog.Builder(this);
        View view=Initialize();
        First();

        ArrayAdapter<String> adapter1=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,photocopy);

        SPhotocopy.setAdapter(adapter1);
        cBphotocopy.setOnClickListener(this);
        click_photocopyItems();


        cBprinnt.setOnClickListener(this);
        ArrayAdapter<String> adapter2=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,print);
        SPrint.setAdapter(adapter2);
        click_printItems();
        cBtransfer.setOnClickListener(this);

        ArrayAdapter<String> adapter3=new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,transfer);

        Stransfer.setAdapter(adapter3);
        click_transfer_items();



        btPC1.setOnClickListener(this);
        btPC2.setOnClickListener(this);
        btPC3.setOnClickListener(this);
        btPR1.setOnClickListener(this);
        btPR2.setOnClickListener(this);
        btTR1.setOnClickListener(this);
        btTR2.setOnClickListener(this);
        btTR3.setOnClickListener(this);
        btTR4.setOnClickListener(this);
        calculate.setOnClickListener(this);



        builder.setView(view);
        builder.setCancelable(true);
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                qn1=qn2=qn3=qn4=qn5=0;
                c1=c2=c3=c4=c5=c6=c7=c8=c9=0;
                buffer.delete(0,buffer.length());
            }
        });
        dialog=builder.create();
        dialog.show();
    }


    private View Initialize() {
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_view, null);
        etName=view.findViewById(R.id.eTname);
        cBphotocopy = view.findViewById(R.id.photocopy);
        SPhotocopy = view.findViewById(R.id.photocopyList);
        etPC1 = view.findViewById(R.id.pheT1);
        btPC1 = view.findViewById(R.id.phBtn1);
        etPC2 = view.findViewById(R.id.pheT2);
        btPC2 = view.findViewById(R.id.phBtn2);
        etPC3 = view.findViewById(R.id.pheT3);
        btPC3 = view.findViewById(R.id.phBtn3);
        cBprinnt = view.findViewById(R.id.print);
        SPrint = view.findViewById(R.id.printList);
        etPR1 = view.findViewById(R.id.preT1);
        etPR2 = view.findViewById(R.id.preT2);
        btPR1 = view.findViewById(R.id.prBtn1);
        btPR2 = view.findViewById(R.id.prBtn2);
        cost1=view.findViewById(R.id.cost1);
        cost2=view.findViewById(R.id.cost2);
        cost3=view.findViewById(R.id.cost3);
        cost4=view.findViewById(R.id.cost4);
        cost5=view.findViewById(R.id.cost5);
        cBtransfer=view.findViewById(R.id.transfer);
        Stransfer=view.findViewById(R.id.tranfer_list);
        etTR1=view.findViewById(R.id.etTR1);
        etTR2=view.findViewById(R.id.etTR2);
        etTR3=view.findViewById(R.id.etTR3);
        etTR4=view.findViewById(R.id.etTR4);
        btTR1=view.findViewById(R.id.btTR1);
        btTR2=view.findViewById(R.id.btTR2);
        btTR3=view.findViewById(R.id.btTR3);
        btTR4=view.findViewById(R.id.btTR4);
        calculate = view.findViewById(R.id.calculate);
        update_db=view.findViewById(R.id.update_db);
        return view;
    }
    private void First()
    {
        SPhotocopy.setVisibility(View.GONE);
        SPrint.setVisibility(View.GONE);
        etPC1.setVisibility(View.GONE);
        btPC1.setVisibility(View.GONE);
        etPC2.setVisibility(View.GONE);
        btPC2.setVisibility(View.GONE);
        etPC3.setVisibility(View.GONE);
        btPC3.setVisibility(View.GONE);
        etPR1.setVisibility(View.GONE);
        etPR2.setVisibility(View.GONE);
        btPR1.setVisibility(View.GONE);
        btPR2.setVisibility(View.GONE);
        cost1.setVisibility(View.GONE);
        cost2.setVisibility(View.GONE);
        cost3.setVisibility(View.GONE);
        cost4.setVisibility(View.GONE);
        cost5.setVisibility(View.GONE);
        etTR1.setVisibility(View.GONE);
        etTR2.setVisibility(View.GONE);
        etTR3.setVisibility(View.GONE);
        etTR4.setVisibility(View.GONE);
        btTR1.setVisibility(View.GONE);
        btTR2.setVisibility(View.GONE);
        btTR3.setVisibility(View.GONE);
        btTR4.setVisibility(View.GONE);
        Stransfer.setVisibility(View.GONE);
        update_db.setVisibility(View.GONE);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.photocopy:
                if(cBphotocopy.isChecked())
                {
                    SPhotocopy.setVisibility(View.VISIBLE);
                }
                else
                {
                    SPhotocopy.setVisibility(View.GONE);

                }
                break;
            case R.id.phBtn1:
                qn1=Double.parseDouble(etPC1.getText().toString());
                c1=Double.parseDouble(cost1.getText().toString());
                etPC1.setVisibility(View.GONE);
                btPC1.setVisibility(View.GONE);
                cost1.setVisibility(View.GONE);
                break;
            case R.id.phBtn2:
                qn2=Double.parseDouble(etPC2.getText().toString());
                etPC2.setVisibility(View.GONE);
                btPC2.setVisibility(View.GONE);
                c2=Double.parseDouble(cost2.getText().toString());
                cost2.setVisibility(View.GONE);
                break;
            case R.id.phBtn3:
                qn3=Double.parseDouble(etPC3.getText().toString());
                etPC3.setVisibility(View.GONE);
                btPC3.setVisibility(View.GONE);
                c3=Double.parseDouble(cost3.getText().toString());
                cost3.setVisibility(View.GONE);
                break;

            case R.id.print:
                if(cBprinnt.isChecked())
                {
                    SPrint.setVisibility(View.VISIBLE);
                }
                else
                {
                    SPrint.setVisibility(View.GONE);

                }
                break;
            case R.id.prBtn1:
                qn4=Double.parseDouble(etPR1.getText().toString());
                c4=Double.parseDouble(cost4.getText().toString());
                cost4.setVisibility(View.GONE);
                etPR1.setVisibility(View.GONE);
                btPR1.setVisibility(View.GONE);
                break;
            case R.id.prBtn2:
                qn5=Double.parseDouble(etPR2.getText().toString());
                c5=Double.parseDouble(cost5.getText().toString());
                etPR2.setVisibility(View.GONE);
                btPR2.setVisibility(View.GONE);
                cost5.setVisibility(View.GONE);
                break;
            case R.id.transfer:
                if(cBtransfer.isChecked())
                {
                    Stransfer.setVisibility(View.VISIBLE);
                }
                else
                {
                    Stransfer.setVisibility(View.GONE);

                }
                break;
            case R.id.btTR1:
                c6=Double.parseDouble(etTR1.getText().toString());
                etTR1.setVisibility(View.GONE);
                btTR1.setVisibility(View.GONE);
                break;
            case R.id.btTR2:
                c7=Double.parseDouble(etTR2.getText().toString());
                etTR2.setVisibility(View.GONE);
                btTR2.setVisibility(View.GONE);
                break;

            case R.id.btTR3:
                c8=Double.parseDouble(etTR3.getText().toString());
                etTR3.setVisibility(View.GONE);
                btTR3.setVisibility(View.GONE);
                break;

            case R.id.btTR4:
                c9=Double.parseDouble(etTR4.getText().toString());
                etTR4.setVisibility(View.GONE);
                btTR4.setVisibility(View.GONE);
                break;



            case R.id.calculate:
                total=(qn1*c1)+(qn2*c2)+(qn3*c3)+(qn4*c4)+(qn5*c5)+c6+c7+c8+c9;
                if(total==0||etName.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter data", Toast.LENGTH_SHORT).show();

                }
                else
                {

                    insertData();
                    dialog.dismiss();
                }
                break;

            default:
                break;
        }
    }

    private void click_photocopyItems()
    {
        SPhotocopy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        break;
                    case 1:
                        etPC1.setVisibility(View.VISIBLE);
                        cost1.setVisibility(View.VISIBLE);
                        btPC1.setVisibility(View.VISIBLE);

                        break;
                    case 2:
                        etPC2.setVisibility(View.VISIBLE);
                        cost2.setVisibility(View.VISIBLE);
                        btPC2.setVisibility(View.VISIBLE);

                        break;
                    case 3:
                        etPC3.setVisibility(View.VISIBLE);
                        cost3.setVisibility(View.VISIBLE);
                        btPC3.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void click_printItems()
    {
        SPrint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        etPR1.setVisibility(View.VISIBLE);
                        cost4.setVisibility(View.VISIBLE);
                        btPR1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        etPR2.setVisibility(View.VISIBLE);
                        cost5.setVisibility(View.VISIBLE);
                        btPR2.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void click_transfer_items() {

        Stransfer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        etTR1.setVisibility(View.VISIBLE);
                        btTR1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        etTR2.setVisibility(View.VISIBLE);
                        btTR2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        etTR3.setVisibility(View.VISIBLE);
                        btTR3.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        etTR4.setVisibility(View.VISIBLE);
                        btTR4.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


        private void insertData()
    {

        total_photocopy=qn1+qn2+qn3;
        total_print=qn4+qn5;
        total_transfer=c6+c7+c8+c9;
        mydb=new MyDataBaseHelper(this);
        boolean isinserted=mydb.insert_table(etName.getText().toString(),total_photocopy,total_print,total_transfer,total);
        if(isinserted) {
            Toast.makeText(this, "Sucessfully added", Toast.LENGTH_SHORT).show();
            ApplicationClass.photcopyList.add(new Photcopy(etName.getText().toString(),total_photocopy,total_print,total_transfer,total));
            adapter.notifyDataSetChanged();

        }
        else
        {
           Toast.makeText(this,"Sucessfully not added",Toast.LENGTH_SHORT).show();
        }



    }


}
