package com.example.exam_progect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class CryptoActivity extends AppCompatActivity {

    private static final String FILE_NAME = "crypto.txt";

    private Spinner spinner;
    private EditText editText, textLeft, textRight, textFile;

    private TextView showText;
    private Intent myFileIntent;
    private RadioButton radioButtonCeasar, radioButtonWordCode;

    private ClipboardManager clipboardManager;
    private ClipData clipData;

    ViewGroup parent;
    private Context context;
    private Uri fileUri;
    private String filePath;
    public static final int PICKFILE_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto);

        spinner = findViewById(R.id.spinner);
        editText = findViewById(R.id.editTextTextMultiLine);
        textLeft = findViewById(R.id.editText_left);
        textRight = findViewById(R.id.editText_right);
        textFile = findViewById(R.id.textFile);
        showText = findViewById(R.id.textView);
        ImageButton btnFilePicker = findViewById(R.id.button_filepicker);
        ImageButton btnCopy = findViewById(R.id.button_copy);
        ImageButton btnSave = findViewById(R.id.button_save);
        Button btnCode = findViewById(R.id.button_code);
        Button btnDecode = findViewById(R.id.button_decode);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.codes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        clipboardManager = (ClipboardManager)this.getSystemService(CLIPBOARD_SERVICE);
        myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);

        radioButtonCeasar = findViewById(R.id.radioButton_ceasar);
        radioButtonWordCode = (RadioButton) findViewById(R.id.radioButton_wordcode);
        radioButtonCeasar.setChecked(true);

        textLeft.setHint(R.string.hint_key_ceaser);
        textLeft.setInputType(InputType.TYPE_CLASS_NUMBER);
        textRight.setVisibility(View.INVISIBLE);

        Button statisticButton = (Button) findViewById(R.id.statistic_button);
        statisticButton.setOnClickListener( v -> {
            Intent intent = new Intent(CryptoActivity.this, PieChartActivity.class);
            intent.putExtra("data", editText.getText().toString().toLowerCase());
            startActivity(intent);
        });

        btnFilePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionsUtils.WriteExternalStorage.isNeedRequest(CryptoActivity.this)) {
                    PermissionsUtils.WriteExternalStorage.request(CryptoActivity.this);
                } else {
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("text/plain");
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveText("Encrypted_text");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = showText.getText().toString();
                clipData = ClipData.newPlainText("text",text);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(CryptoActivity.this,R.string.toast_text_is_copied,Toast.LENGTH_SHORT).show();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:

                        break;
                    case R.id.radioButton_ceasar:
                        textRight.setVisibility(View.INVISIBLE);
                        textLeft.setHint(R.string.hint_key_ceaser);
                        textLeft.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    case R.id.radioButton_wordcode:
//                        textRight.setVisibility(View.VISIBLE);
//                        textRight.setHint(R.string.hint_key_wordcode_right);
//                        textLeft.setHint(R.string.hint_key_wordcode_left);
//                        textLeft.setInputType(InputType.TYPE_CLASS_TEXT);
//                        textRight.setInputType(InputType.TYPE_CLASS_TEXT);
                        textLeft.setHint(R.string.hint_key_wordcode_left);
                        textLeft.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                }
            }
        });

        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String codedText = "";
                if(textLeft.length() == 0){
                    textLeft.setError("Впишіть ключ");
                }
                else{
                    String text = editText.getText().toString();
                    String selectedLanguage = spinner.getSelectedItem().toString();
                    if (radioButtonCeasar.isChecked()) {
                        String key = textLeft.getText().toString();
                        codedText = Cryptographer.caesarСipher(text, selectedLanguage, Integer.parseInt(key));
                    } else if (radioButtonWordCode.isChecked()) {
//                    String keyRow = textLeft.getText().toString();
//                    String keyCol = textRight.getText().toString();
                        String key = textLeft.getText().toString();
                        codedText = Cryptographer.wordCode(text, selectedLanguage, key);
//                    String key = textLeft.getText().toString();
//                    codedText = Cryptographer.wordCode()
                    }
                    showText.setText(codedText);
                }
//                CryptoFragmentDirections.ActionNavCryptoToNavPiechart cryptoToPieChartAction =
//                        CryptoFragmentDirections.actionNavCryptoToNavPiechart().setData(editText.getText().toString().toLowerCase());
//                Navigation.findNavController(v).navigate(cryptoToPieChartAction);
            }
        });

        btnDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String decodedText = "";
                if(textLeft.length() == 0){
                    textLeft.setError("Впишіть ключ");
                }
                else {
                    String text = editText.getText().toString();
                    String selectedLanguage = spinner.getSelectedItem().toString();
                    if (radioButtonCeasar.isChecked()) {
                        String key = textLeft.getText().toString();
                        decodedText = Cryptographer.caesarCipherDecode(text, selectedLanguage, Integer.parseInt(key));
                    } else if (radioButtonWordCode.isChecked()) {
//                    String keyRow = textLeft.getText().toString();
//                    String keyCol = textRight.getText().toString();
                        String key = textLeft.getText().toString();
                        decodedText = Cryptographer.wordCodeDecode(text, selectedLanguage, key);
                    }
                    showText.setText(decodedText);
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    filePath = fileUri.getPath();
                    System.out.println(fileUri.toString());
                    textFile.setText(filePath);
                    //readFile(filePath);
                    //openText(filePath);
                }
                break;
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void saveText(String fileName) throws IOException {
        File file;

        file = new File(this.getFilesDir(), fileName);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            String text = showText.getText().toString();

            File gpxfile = new File(file, fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(showText.getText().toString());
            writer.flush();
            writer.close();
            Toast.makeText(this, "Файл збережено", Toast.LENGTH_SHORT).show();
        }
        catch(IOException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void readFile(String filePath) {
        String jString = null;
        StringBuilder builder = new StringBuilder();

        File yourFile = new File(filePath);
        Log.d("CryptoFragment", yourFile.exists() + " " + filePath);
        if (yourFile.exists()) {

            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(yourFile));
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Пустий файл!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            String row = "";
            try {
                if(bufferedReader != null){
                    while ((row = bufferedReader.readLine()) != null) {
                        builder.append(row + "\n");
                    }
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Щось пішло не так", Toast.LENGTH_SHORT).show();
            }
            jString = builder.toString();
        }
        else {
            Toast.makeText(this, "Файлу не існує", Toast.LENGTH_SHORT).show();
            Log.i("FAIL", "FILE NOT FOUND");
        }

        editText.setText(jString);
    }

    public void onClickLoad(View view) {
        editText.setText("");
        textFile.setText(FILE_NAME);
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String LoadText;

            while ((LoadText = br.readLine()) != null){
                sb.append(LoadText).append("\n");
            }
            editText.setText(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void onClickSave(View view) {
        String TextToSave = "";
        TextToSave = showText.getText().toString();
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(TextToSave.getBytes());
            Toast.makeText(this,
                    "Save to " + getFilesDir() + "/" + FILE_NAME,
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}