package com.example.oways.sqlitecrud;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String DATABASE_NAME="mydatabase";
    SQLiteDatabase mDatabase;
    EditText editTextName,editTextSalary;
    Spinner spinnerDept;
    Button mButtonAddEmployee;
    TextView mtextViewEmployee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        createTable();
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextSalary=(EditText)findViewById(R.id.editTextSalary);
spinnerDept=(Spinner)findViewById(R.id.spinnerDepartment);
        mButtonAddEmployee=(Button)findViewById(R.id.buttonAddEmployee);
        mButtonAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
AddEmployee();
            }
        });
        mtextViewEmployee=(TextView)findViewById(R.id.textViewViewEmployees);
        mtextViewEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),EmployeeActivity.class));
            }
        });
    }

    private void AddEmployee() {
        String name=editTextName.getText().toString().trim();
        String salary=editTextSalary.getText().toString().trim();
        String dept=spinnerDept.getSelectedItem().toString();

        if(name.isEmpty()){
            editTextName.setError("Name can't be empty");
            editTextName.requestFocus();
            return;
        }
        if(salary.isEmpty()){
            editTextSalary.setError("ID can't be empty");
            editTextSalary.requestFocus();
            return;
        }
        //getting the current time for joining date
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");
        String joiningDate = sdf.format(cal.getTime());

String sql="INSERT INTO employees(name,department,joiningdate,salary)"+
        "VALUES(?, ?, ?, ?)";
        mDatabase.execSQL(sql,new String[]{name,dept,joiningDate,salary});
        Toast.makeText(this, "Student Added!", Toast.LENGTH_SHORT).show();
    }

    private void createTable(){
        String sql="CREATE TABLE IF NOT EXISTS employees (\n" +
                "    id INTEGER NOT NULL CONSTRAINT employees_pk PRIMARY KEY AUTOINCREMENT,\n" +
                "    name varchar(200) NOT NULL,\n" +
                "    department varchar(200) NOT NULL,\n" +
                "    joiningdate datetime NOT NULL,\n" +
                "    salary double NOT NULL\n" +
                ");";
    mDatabase.execSQL(sql);
    }

}
