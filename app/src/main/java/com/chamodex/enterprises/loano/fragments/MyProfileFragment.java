package com.chamodex.enterprises.loano.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chamodex.enterprises.loano.R;
import com.chamodex.enterprises.loano.models.Users;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;
import org.threeten.bp.ZoneId;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class MyProfileFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference reference;
    ShapeableImageView proPic;
    TextView nameDetails, uidDetails, addressMainDetail, dobDetails, addressDetails, addressDetailsBtn, phoneDetails, emailDetails, genderDetails, accountDetails;
    AlertDialog dialog;
    EditText editText;
    DatePickerDialog datePickerDialog;
    private Pattern accNoPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    public MyProfileFragment(FirebaseAuth auth) {
        // Required empty public constructor
        this.auth = auth;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        // content here
        // get views
        proPic = view.findViewById(R.id.item_profile_pic_detail);
        nameDetails = view.findViewById(R.id.item_name_detail);
        uidDetails = view.findViewById(R.id.item_uid);
        addressMainDetail = view.findViewById(R.id.item_address_main);
        addressDetails = view.findViewById(R.id.item_address_details);
        addressDetailsBtn = view.findViewById(R.id.item_address_details_btn);
        dobDetails = view.findViewById(R.id.item_dob);
        phoneDetails = view.findViewById(R.id.item_phone_detail);
        emailDetails = view.findViewById(R.id.item_email_details);
        genderDetails = view.findViewById(R.id.item_gender);
        accountDetails = view.findViewById(R.id.item_account_no);
        // initialize
        AndroidThreeTen.init(getActivity());
        initDatePicker();
        dialog = new AlertDialog.Builder(getActivity()).create();
        editText = new EditText(getActivity());
        // database
        db = FirebaseDatabase.getInstance();

        setDefaultTexts();
        editing();

        // content end
        return view;
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                LocalDate birthDate = LocalDate.of(year, month, dayOfMonth);
                int age = Period.between(birthDate, LocalDate.now()).getYears();
                if (age < 18) {
                    Toast.makeText(getActivity(), "Your age must be greater than 18! Otherwise you could't proceed!", Toast.LENGTH_SHORT).show();
                } else {
                    dobDetails.setText(date);
                    Users users = new Users(auth.getCurrentUser().getEmail(), nameDetails.getText().toString(), phoneDetails.getText().toString(), addressDetails.getText().toString(), dobDetails.getText().toString(), genderDetails.getText().toString(), accountDetails.getText().toString());
                    // saving
                    selector = 5;
                    saveData(selector, users);
                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, dateSetListener, year, month, day);
//        dobDetails.setText(getTodaysDate());
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + dayOfMonth + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "JAN";
        }
    }

    int selector;
    private void editing() {
        dialog.setTitle("Edit the text");
        dialog.setView(editText);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "SAVE", (dialog, which) -> {
            Users users = new Users(auth.getCurrentUser().getEmail(), nameDetails.getText().toString(), phoneDetails.getText().toString(), addressDetails.getText().toString(), dobDetails.getText().toString(), genderDetails.getText().toString(), accountDetails.getText().toString());
            if (selector == 1) {
                // name change
                if (editText.getText() == null) {
                    selector = 0;
                    Toast.makeText(getActivity(), "Must enter your name here", Toast.LENGTH_SHORT).show();
                } else {
                    nameDetails.setText(editText.getText());
                    users.setName(nameDetails.getText().toString());
                }
            } else if (selector == 2) {
                // address change
                if (editText.getText() == null) {
                    selector = 0;
                    Toast.makeText(getActivity(), "Must enter your address here", Toast.LENGTH_SHORT).show();
                } else {
                    String address = editText.getText().toString().replace(", ", ",");
                    address = editText.getText().toString().replace(",\n", ",");
                    String[] addressSplitByComma = address.split(",");
                    StringBuilder properAddress = new StringBuilder();
                    int i = 0;
                    for (String s : addressSplitByComma) {
                        i++;
                        properAddress.append(s);
                        if (i != addressSplitByComma.length) {
                            properAddress.append(",");
                            properAddress.append("\n");
                        }
                    }
                    addressMainDetail.setText(addressSplitByComma[0]);
                    addressDetails.setText(properAddress.toString());
                    users.setAddress(address);
                }
            } else if (selector == 3) {
                if (editText.getText() == null) {
                    selector = 0;
                    Toast.makeText(getActivity(), "Must enter your phone number here", Toast.LENGTH_SHORT).show();
                } else {
                    phoneDetails.setText(editText.getText());
                    users.setMobile(phoneDetails.getText().toString());
                }
            } else if (selector == 4) {
                if (editText.getText() == null) {
                    selector = 0;
                    Toast.makeText(getActivity(), "Must enter your account number here", Toast.LENGTH_SHORT).show();
                } else {
                    if (accNoPattern.matcher(editText.getText().toString()).matches()) {
                        accountDetails.setText(editText.getText());
                        users.setAccountNo(accountDetails.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "Please enter valid account number", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            // saving
            saveData(selector, users);

        });

        // name editing
        nameDetails.setOnClickListener(view -> {
            selector = 1;
            dialog.setTitle("Enter your name");
            editText.setText(nameDetails.getText());
            dialog.show();
        });
        // address editing
        addressDetailsBtn.setOnClickListener(view -> {
            selector = 2;
            dialog.setTitle("Enter your address");
            editText.setText(addressDetails.getText());
            dialog.show();
        });
        // phone editing
        phoneDetails.setOnClickListener(v -> {
            selector = 3;
            dialog.setTitle("Enter your address");
            editText.setText(phoneDetails.getText());
            dialog.show();
        });
        // set account details
        accountDetails.setOnClickListener(v -> {
            if (!accountDetails.getText().toString().equals("not-set")) {
                Toast.makeText(getActivity(), "Your account number is already set!", Toast.LENGTH_SHORT).show();
            } else {
                selector = 4;
                dialog.setTitle("Enter your account number");
                editText.setText(accountDetails.getText());
                dialog.show();
            }
        });
        // dob edit
        dobDetails.setOnClickListener(v -> {
            if (!dobDetails.getText().toString().equals("not-set")) {
                Toast.makeText(getActivity(), "Your birth date is already set!", Toast.LENGTH_SHORT).show();
            } else {
                datePickerDialog.setTitle("Select date of birth");
                datePickerDialog.show();
            }
        });
        genderDetails.setOnClickListener(v -> {
            if (!genderDetails.getText().toString().equals("not-set")) {
                Toast.makeText(getActivity(), "Your gender is already set!", Toast.LENGTH_SHORT).show();
            } else {
                showRadioConfirmationDialog();
            }
        });
    }
    int selectedItemIndex = 0;
    String selectedGender;
    private void showRadioConfirmationDialog() {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(getActivity());
        String[] genderList = {"Male", "Female"};
        selectedGender = genderList[selectedItemIndex];
        materialAlertDialogBuilder
                .setTitle("Select gender")
                .setSingleChoiceItems(genderList, selectedItemIndex, (dialog, which) -> {
                    selectedItemIndex = which;
                    selectedGender = genderList[which];
                })
                .setPositiveButton("Ok", (dialog, which) -> {
                    Users users = new Users(auth.getCurrentUser().getEmail(), nameDetails.getText().toString(), phoneDetails.getText().toString(), addressDetails.getText().toString(), dobDetails.getText().toString(), genderDetails.getText().toString(), accountDetails.getText().toString());
                    users.setGender(selectedGender);
                    // saving
                    selector = 6;
                    saveData(selector, users);
                })
                .setNeutralButton("Cancel", (dialog, which) -> {

                })
                .show();

    }

    private void saveData(int selector, Users users) {
        if (selector != 0) {
            if (!users.getAddress().equals("not-set") || users.getAddress() != null) {
                String address = users.getAddress().replace(", ", ",");
                address = users.getAddress().replace(",\n", ",");
                String[] addressSplitByComma = address.split(",");
                StringBuilder properAddress = new StringBuilder();
                int i = 0;
                for (String s : addressSplitByComma) {
                    i++;
                    properAddress.append(s);
                    if (i != addressSplitByComma.length) {
                        properAddress.append(",");
                        properAddress.append("\n");
                    }
                }
                addressMainDetail.setText(addressSplitByComma[0]);
                addressDetails.setText(properAddress.toString());
                users.setAddress(address);
            }
            reference = db.getReference("Users");
            reference.child(auth.getCurrentUser().getUid()).setValue(users).addOnCompleteListener(t -> {
                if (t.isSuccessful()) {
                    Toast.makeText(getActivity(), "Details saved successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed to save data!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    Users user;
    private void setDefaultTexts() {
        user = new Users();
        reference = db.getReference("Users");
        reference.child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(Users.class);
                    nameDetails.setText(user.getName());
                    uidDetails.setText("uid: "+auth.getCurrentUser().getUid());
                    phoneDetails.setText(user.getMobile());
                    emailDetails.setText(user.getEmail());
                    dobDetails.setText(user.getDob());
                    genderDetails.setText(user.getGender());
                    accountDetails.setText(user.getAccountNo());

                    // address set
                    String[] addressSplitByComma = user.getAddress().split(",");
                    StringBuilder properAddress = new StringBuilder();
                    int i = 0;
                    for (String s : addressSplitByComma) {
                        i++;
                        properAddress.append(s);
                        if (i != addressSplitByComma.length) {
                            properAddress.append(",");
                            properAddress.append("\n");
                        }
                    }
                    addressMainDetail.setText(addressSplitByComma[0]);
                    addressDetails.setText(properAddress.toString());
                } else {
                    nameDetails.setText("not-set");
                    uidDetails.setText("uid: "+auth.getCurrentUser().getUid());
                    addressMainDetail.setText("not-set");
                    addressDetails.setText("not-set");
                    phoneDetails.setText("not-set");
                    emailDetails.setText(auth.getCurrentUser().getEmail());
                    dobDetails.setText("not-set");
                    genderDetails.setText("not-set");
                    accountDetails.setText("not-set");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed: " + error.getCode());
                Toast.makeText(getActivity(), "Database reading failed: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
//        reference.child(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (task.isSuccessful()) {
//                    if (task.getResult().exists()) {
//                        user = task.getResult().getValue(Users.class);
//                        nameDetails.setText(user.getName());
//                        uidDetails.setText("uid: "+auth.getCurrentUser().getUid());
//                        phoneDetails.setText(user.getMobile());
//                        emailDetails.setText(user.getEmail());
//                        dobDetails.setText(user.getDob());
//
//                        // address set
//                        String[] addressSplitByComma = user.getAddress().split(",");
//                        StringBuilder properAddress = new StringBuilder();
//                        int i = 0;
//                        for (String s : addressSplitByComma) {
//                            i++;
//                            properAddress.append(s);
//                            if (i != addressSplitByComma.length) {
//                                properAddress.append(",");
//                                properAddress.append("\n");
//                            }
//                        }
//                        addressMainDetail.setText(addressSplitByComma[0]);
//                        addressDetails.setText(properAddress.toString());
//                    } else {
//                        nameDetails.setText("not-set");
//                        uidDetails.setText("uid: "+auth.getCurrentUser().getUid());
//                        addressMainDetail.setText("not-set");
//                        addressDetails.setText("not-set");
//                        phoneDetails.setText("not-set");
//                        emailDetails.setText(auth.getCurrentUser().getEmail());
//                        dobDetails.setText("not-set");
//                    }
//                } else {
//                    Toast.makeText(getActivity(), "Failed to read data!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}