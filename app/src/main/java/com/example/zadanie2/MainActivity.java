package com.example.zadanie2;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.zadanie2.tasks.DeleteDialog;
import com.example.zadanie2.tasks.TaskListContent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskFragment.OnListFragmentInteractionListener, DeleteDialog.OnDeleteDialogInteractionListener {
    private File storageDir;
    private String mCurrentPhotoPath;
    public static final String taskExtra = "taskExtra";
    private int currentItemPosition = -1;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private TaskListContent.Task currentTask;
    private final String CURRENT_TASK_KEY = "CurrentTask";
    private final String TASKS_JSON_FILE = "tasks.json";
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        if (savedInstanceState != null) {
            currentTask = savedInstanceState.getParcelable(CURRENT_TASK_KEY);
        }
        restoreFromJson ();
    }

    private void startSecondActivity(TaskListContent.Task task, int position)
    {
        Intent intent =  new Intent ( this, TaskInfoActivity.class );
        intent.putExtra ( taskExtra, ( Parcelable ) task );
        startActivity ( intent );
    }

    @Override
    public void OnDeleteClick(int position) {
        showDeleteDialog ();
        currentItemPosition=position;
    }

    @Override
    public void onListFragmentClickInteraction(TaskListContent.Task task, int position)
    {
        currentTask = task;
            Toast.makeText ( this, getString ( R.string.nazwa ) + position, Toast.LENGTH_SHORT ).show ();
            if (getResources ().getConfiguration ().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                displayTaskInFragment ( task );
            } else {
                startSecondActivity ( task, position );
            }
    }

    private void displayTaskInFragment(TaskListContent.Task task)
    {
        TaskInfoFragment taskInfoFragment = ((TaskInfoFragment) getSupportFragmentManager ().findFragmentById ( R.id.displayFragment ));
        if(taskInfoFragment != null)
        {
            taskInfoFragment.displayTask ( task );
        }
    }
    private  void showDeleteDialog()
    {

        DeleteDialog.newInstance().show(getSupportFragmentManager (), getString ( R.string.hello_blank_fragment ));
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        if(currentItemPosition != -1 && currentItemPosition < TaskListContent.ITEMS.size ())
        {
            TaskListContent.removeItem(currentItemPosition);
            ((TaskFragment) getSupportFragmentManager ().findFragmentById ( R.id.taskFragment )).notifyDataChange ();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        View v =findViewById ( R.id.floatingActionButton );
            if(v != null)
            {
            Snackbar.make(v, getString(R.string.delete_cancel_msg), Snackbar.LENGTH_LONG).setAction ( getString ( R.string.retray_msg ), new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    showDeleteDialog ();
                }
            } ).show ();
        }
    }

    public void addNextContact(View view) {
        //FloatingActionButton floatingActionButton = findViewById ( R.id.floatingActionButton );
        Intent intent = new Intent ( getApplicationContext (), AddContact.class );
        startActivityForResult ( intent, SECOND_ACTIVITY_REQUEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        ((TaskFragment) getSupportFragmentManager ().findFragmentById ( R.id.taskFragment )).notifyDataChange ();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            Intent intent = new Intent(getApplicationContext(),AddContact.class);
            intent.putExtra("photo", mCurrentPhotoPath);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(currentTask != null)
            outState.putParcelable ( CURRENT_TASK_KEY, currentTask );
        super.onSaveInstanceState ( outState );
    }

    @Override
    protected void onResume()
    {
        super.onResume ();
        ((TaskFragment) getSupportFragmentManager ().findFragmentById ( R.id.taskFragment )).notifyDataChange ();
        if(getResources ().getConfiguration ().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            if(currentTask != null)
            {
                displayTaskInFragment ( currentTask );
            }
        }
    }

    private void saveTasksToJson()
    {
        Gson gson = new Gson ();
        String listJson = gson.toJson ( TaskListContent.ITEMS );
        FileOutputStream outputStream;
        try{
            outputStream = openFileOutput ( TASKS_JSON_FILE, MODE_PRIVATE );
            FileWriter writer = new FileWriter ( outputStream.getFD ());
            writer.write ( listJson );
            writer.close ();

        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    public void restoreFromJson(){
        FileInputStream inputStream;
        int DEFAULT_BUFFER_SIZE = 10000;
        Gson gson = new Gson ();
        String readJson;

        try{
            inputStream = openFileInput ( TASKS_JSON_FILE );
            FileReader reader = new FileReader ( inputStream.getFD () );
            char[] buf = new char[DEFAULT_BUFFER_SIZE];
            int n;
            StringBuilder builder = new StringBuilder (  );
            while ((n = reader.read (buf)) >= 0)
            {
                String tmp = String.valueOf ( buf );
                String substring = (n<DEFAULT_BUFFER_SIZE) ? tmp.substring ( 0, n ) : tmp;
                builder.append ( substring );
            }
            reader.close ();
            readJson = builder.toString ();
            Type collectionType = new TypeToken<List<TaskListContent.Task>> (){}.getType ();
            List<TaskListContent.Task> o = gson.fromJson ( readJson, collectionType );
            if(o != null)
            {
                TaskListContent.clearList ();
                for(TaskListContent.Task task : o)
                    TaskListContent.addItem ( task );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    @Override
    public void onDestroy()
    {
        saveTasksToJson ();
        super.onDestroy();
    }

    public void addContactThroughPhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(),
                        getString(R.string.myFileprovider),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "photo" + timeStamp + "_";
        storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
