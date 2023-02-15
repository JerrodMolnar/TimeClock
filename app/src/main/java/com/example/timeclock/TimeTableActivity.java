package com.example.timeclock;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class TimeTableActivity extends AppCompatActivity
{
    private LinkedList<String> arrLogs = new LinkedList<>();
    private LinkedList<CheckBox> arrChkEdits = new LinkedList<>();
    private LinkedList<CheckBox> arrChkRemoves = new LinkedList<>();
    private TimeDifference timeDifference = new TimeDifference();
    int index = 0;
    TableLayout tbl;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks()
        {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState)
            {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity)
            {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity)
            {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity)
            {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity)
            {
                SaveEdits();
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState)
            {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity)
            {

            }
        });
        setContentView(R.layout.activity_time_table);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        arrLogs = ReadFile();
        CreateTable();
    }

    private void SaveEdits()
    {
        LinkedList<String> llstOriginal = ReadFile();
        boolean isDifferent = false;
        if (llstOriginal.size() != arrLogs.size())
        {
            isDifferent = true;
        } else
        {
            for (int i = 0; i < arrLogs.size(); i++)
            {
                if (arrLogs.get(i).equals(llstOriginal.get(i)))
                {
                    isDifferent = false;

                } else
                {
                    isDifferent = true;
                    break;
                }
            }
        }

        if (isDifferent)
        {
            try
            {
                FileOutputStream fosWriter = openFileOutput("WorkDays.txt", MODE_PRIVATE);
                for (int i = 0; i < arrLogs.size(); i++)
                {
                    String strWrite = arrLogs.get(i) + "\n";
                    fosWriter.write(strWrite.getBytes());
                }
                fosWriter.close();
                Toast.makeText(getBaseContext(), "Times Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e)
            {
                Log.e("SaveEdits", e.getMessage());
            }
        }
    }

    private void RemoveRecord(View v)
    {
        //for (int i = 0; i < arrChkEdits.size(); i++)

        for (int i = 0; i < arrChkRemoves.size(); i++)
        {
            arrChkEdits.get(i).setEnabled(false);
            arrChkRemoves.get(i).setEnabled(false);
            if (v == arrChkRemoves.get(i))
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Remove " + arrLogs.get(i));
                alert.setMessage("Are you sure you want to remove?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        for (int i = 0; i < arrChkEdits.size(); i++)
                        {
                            arrChkEdits.get(i).setEnabled(true);
                            arrChkRemoves.get(i).setEnabled(true);
                            arrChkEdits.get(i).setChecked(false);
                            arrChkRemoves.get(i).setChecked(false);
                        }
                    }
                });

                final AlertDialog dialog = alert.create();
                dialog.show();
                int finalI = i;
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        arrLogs.remove(finalI);
                        Toast.makeText(getBaseContext(), "Time Removed", Toast.LENGTH_SHORT).show();
                        arrChkEdits.remove(finalI);
                        arrChkRemoves.remove(finalI);
                        for (int i = 0; i < arrChkEdits.size(); i++)
                        {
                            arrChkEdits.get(i).setEnabled(true);
                            arrChkRemoves.get(i).setEnabled(true);
                            arrChkEdits.get(i).setChecked(false);
                            arrChkRemoves.get(i).setChecked(false);
                        }
                        dialog.dismiss();
                        CreateTable();
                    }
                });
            }
        }
    }

    private void EditRecord(View v)
    {
        index = 0;
        String punchEdit = "";
        for (int i = 0; i < arrChkEdits.size(); i++)
        {
            if (v == arrChkEdits.get(i))
            {
                punchEdit = arrLogs.get(i).toUpperCase();
                index = i;
            }
            arrChkRemoves.get(i).setEnabled(false);
            arrChkEdits.get(i).setEnabled(false);
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Punch Edit");
        final EditText input = new EditText(this);
        input.setText(punchEdit);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                for (int i = 0; i < arrChkEdits.size(); i++)
                {
                    arrChkEdits.get(i).setEnabled(true);
                    arrChkRemoves.get(i).setEnabled(true);
                    arrChkEdits.get(i).setChecked(false);
                    arrChkRemoves.get(i).setChecked(false);
                }
            }
        });

        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String strInput = input.getText().toString().trim().toUpperCase();
                String dateIn = strInput.substring(4, 14);
                String timeIn = strInput.substring(15, 26);
                String dateOut = strInput.substring(33, 43);
                String timeOut = strInput.substring(44, 55);

                timeDifference.FindDifferenceInTimes(dateIn + " " + timeIn, dateOut + " " + timeOut);

                if (!CheckString(strInput) || strInput.length() != 55)
                {
                    Toast.makeText(getBaseContext(), "Invalid Format", Toast.LENGTH_SHORT).show();
                } else if (timeDifference.getDifferenceInTime() <= 0)
                {
                    Toast.makeText(getBaseContext(), "Time out cannot be before time in", Toast.LENGTH_SHORT).show();
                } else
                {
                    arrLogs.set(index, strInput);
                    index = 0;
                    dialog.dismiss();
                    CreateTable();
                    Toast.makeText(getBaseContext(), "Time Saved", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < arrChkEdits.size(); i++)
                {
                    arrChkEdits.get(i).setEnabled(true);
                    arrChkRemoves.get(i).setEnabled(true);
                    arrChkEdits.get(i).setChecked(false);
                    arrChkRemoves.get(i).setChecked(false);
                }
            }
        });
    }

    private Boolean CheckString(String s)
    {
        Boolean isFormatted = false;
        String regex = "\\D\\D\\D\\s\\d\\d/\\d\\d/\\d\\d\\d\\d\\s\\d\\d:\\d\\d:\\d\\d\\s(AM|PM)" +
                "\\s-\\s\\D\\D\\D\\s\\d\\d/\\d\\d/\\d\\d\\d\\d\\s\\d\\d:\\d\\d:\\d\\d\\s(AM|PM)";
        Pattern pattern = Pattern.compile(regex);

        boolean matches = pattern.matcher(s).matches();
        if (!matches)
        {
            isFormatted = false;
        } else
        {
            isFormatted = true;
        }
        return isFormatted;
    }

    private LinkedList<String> ReadFile()
    {
        LinkedList<String> llstFileStrings = new LinkedList<>();
        try
        {
            FileInputStream inputStream = openFileInput("WorkDays.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();

            while (line != null)
            {
                if (!line.isEmpty())
                {
                    llstFileStrings.add(line);
                }
                line = reader.readLine();
            }
            return llstFileStrings;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private void CreateTable()
    {
        String dayOfWeekIn;
        String dateIn;
        String timeIn;
        String dayOfWeekOut;
        String dateOut;
        String timeOut;
        String line;
        int evenCount = 0;

        if (tbl == null)
        {
            tbl = findViewById(R.id.punchTable);
        }
        int childCount = tbl.getChildCount();
        if (childCount > 1)
        {
            tbl.removeViews(1, childCount - 1);
            arrChkEdits.clear();
            arrChkRemoves.clear();
        }

        for (int i = 0; i < arrLogs.size(); i++)
        {
            line = arrLogs.get(i).toUpperCase().trim();
            dayOfWeekIn = line.substring(0, 3);
            dateIn = line.substring(4, 14);
            timeIn = line.substring(15, 26);
            dayOfWeekOut = line.substring(29, 32);
            dateOut = line.substring(33, 43);
            timeOut = line.substring(44, 55);

            TableRow row = new TableRow(this);

            TextView lblDayOfWeekIn = new TextView(this);
            lblDayOfWeekIn.setText(dayOfWeekIn);
            lblDayOfWeekIn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            row.addView(lblDayOfWeekIn);

            TextView lblDateIn = new TextView(this);
            lblDateIn.setText(dateIn);
            lblDateIn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            row.addView(lblDateIn);

            TextView lblTimeIn = new TextView(this);
            lblTimeIn.setText(timeIn);
            lblTimeIn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            row.addView(lblTimeIn);

            TextView lblDayOfWeekOut = new TextView(this);
            lblDayOfWeekOut.setText(dayOfWeekOut);
            lblDayOfWeekOut.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            row.addView(lblDayOfWeekOut);

            TextView lblDateOut = new TextView(this);
            lblDateOut.setText(dateOut);
            lblDateOut.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            row.addView(lblDateOut);

            TextView lblTimeOut = new TextView(this);
            lblTimeOut.setText(timeOut);
            lblTimeOut.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            row.addView(lblTimeOut);

            TextView lblDuration = new TextView(this);
            lblDuration.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            timeDifference.FindDifferenceInTimes(dayOfWeekIn + " " + dateIn + " " + timeIn, dayOfWeekOut + " " + dateOut + " " + timeOut);
            long time = timeDifference.getDifferenceInTime();
            if (time > 0)
            {
                lblDuration.setText(timeDifference.getStrDuration());
            }
            row.addView(lblDuration);

            CheckBox chkEdit = new CheckBox(this);
            chkEdit.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            arrChkEdits.add(chkEdit);
            chkEdit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (((CheckBox) v).isChecked())
                    {
                        EditRecord(v);
                    }
                }
            });
            row.addView(chkEdit);

            CheckBox chkRemove = new CheckBox(this);
            chkRemove.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            arrChkRemoves.add(chkRemove);
            chkRemove.setOnClickListener(v -> RemoveRecord(v));
            row.addView(chkRemove);

            if (evenCount == 1)
            {
                lblDayOfWeekIn.setTextColor(Color.BLACK);
                lblDateIn.setTextColor(Color.BLACK);
                lblTimeIn.setTextColor(Color.BLACK);
                lblDayOfWeekOut.setTextColor(Color.BLACK);
                lblDateOut.setTextColor(Color.BLACK);
                lblTimeOut.setTextColor(Color.BLACK);
                lblDuration.setTextColor(Color.BLACK);
                evenCount = 0;
            } else
            {
                lblDayOfWeekIn.setTextColor(Color.BLUE);
                lblDateIn.setTextColor(Color.BLUE);
                lblTimeIn.setTextColor(Color.BLUE);
                lblDayOfWeekOut.setTextColor(Color.BLUE);
                lblDateOut.setTextColor(Color.BLUE);
                lblTimeOut.setTextColor(Color.BLUE);
                lblDuration.setTextColor(Color.BLUE);
                evenCount++;
            }

            tbl.addView(row);
        }
    }
}