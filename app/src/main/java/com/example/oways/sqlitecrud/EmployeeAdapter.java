package com.example.oways.sqlitecrud;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Oways on 01-Feb-18.
 */

public class EmployeeAdapter extends ArrayAdapter<Employee> {

    Context context;
    int resource;
    List<Employee> employeeList;
    SQLiteDatabase mDatabase;

    public EmployeeAdapter(@NonNull Context context, int resource, @NonNull List<Employee> employeeList,SQLiteDatabase mDatabase) {
        super(context, resource, employeeList);
        this.context=context;
        this.resource=resource;
        this.employeeList=employeeList;
        this.mDatabase=mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resource,null);

        //getting views
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewDept = view.findViewById(R.id.textViewDepartment);
        TextView textViewSalary = view.findViewById(R.id.textViewSalary);
        TextView textViewJoiningDate = view.findViewById(R.id.textViewJoiningDate);

        final Employee employee= employeeList.get(position);
        //adding data to views
        textViewName.setText(employee.getName());
        textViewDept.setText(employee.getDept());
        textViewSalary.setText(String.valueOf(employee.getSalary()));
        textViewJoiningDate.setText(employee.getJoiningdate());
        view.findViewById(R.id.buttonDeleteEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmployee(employee);
            }
        });

        view.findViewById(R.id.buttonEditEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee(employee);
            }
        });
        return view;
    }

    private void updateEmployee(final Employee employee) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.dialog_update_employee,null);
        builder.setView(view);

        final AlertDialog alertDialog=builder.create();
        alertDialog.show();

        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextSalary = view.findViewById(R.id.editTextSalary);
        final Spinner spinnerDepartment = view.findViewById(R.id.spinnerDepartment);

        editTextName.setText(employee.getName());
        editTextSalary.setText(String.valueOf(employee.getSalary()));


                view.findViewById(R.id.buttonUpdateEmployee).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = editTextName.getText().toString().trim();
                        String salary = editTextSalary.getText().toString().trim();
                        String dept = spinnerDepartment.getSelectedItem().toString();

                        if (name.isEmpty()) {
                            editTextName.setError("Name can't be empty");
                            editTextName.requestFocus();
                            return;
                        }
                        if (salary.isEmpty()) {
                            editTextSalary.setError("ID can't be empty");
                            editTextSalary.requestFocus();
                            return;
                        }
                        String sql = "UPDATE employees \n" +
                                "SET name = ?, \n" +
                                "department = ?, \n" +
                                "salary = ? \n" +
                                "WHERE id = ?;\n";

                        mDatabase.execSQL(sql, new String[]{name, dept, salary, String.valueOf(employee.getId())});
                        Toast.makeText(context, "Student Updated", Toast.LENGTH_SHORT).show();
                        LoadEmployfromDatabaseAgain();
                        alertDialog.dismiss();
                    }
                });
    }

    private void deleteEmployee(final Employee employee) {
        AlertDialog.Builder ab=new AlertDialog.Builder(context);
        ab.setTitle("Are you sure?");
        ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            String sql="DELETE FROM employees WHERE id=?";
            mDatabase.execSQL(sql,new Integer[]{employee.getId()});
            LoadEmployfromDatabaseAgain();
            }
        });
        ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog= ab.create();
        alertDialog.show();
    }

    private void LoadEmployfromDatabaseAgain() {
        String sql="select * from employees";
        Cursor cursor=mDatabase.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            employeeList.clear();
            do{
                employeeList.add(new Employee(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4)

                ));
            }while (cursor.moveToNext());

            notifyDataSetChanged();
        }
    }
}
