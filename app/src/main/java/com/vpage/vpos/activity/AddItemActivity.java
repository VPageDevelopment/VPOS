package com.vpage.vpos.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.vpage.vpos.R;
import com.vpage.vpos.tools.ActionEditText;
import com.vpage.vpos.tools.OnNetworkChangeListener;
import com.vpage.vpos.tools.PlayGifView;
import com.vpage.vpos.tools.VTools;
import com.vpage.vpos.tools.utils.AppConstant;
import com.vpage.vpos.tools.utils.LogFlag;
import com.vpage.vpos.tools.utils.NetworkUtil;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
/*import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;*/
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@EActivity(R.layout.activity_additem)
public class AddItemActivity extends AppCompatActivity implements View.OnClickListener, OnNetworkChangeListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = AddItemActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.UPC)
    EditText UPC;

    @ViewById(R.id.itemName)
    EditText itemName;

    @ViewById(R.id.textError)
    TextView textError;

    @ViewById(R.id.category)
    EditText category;

    @ViewById(R.id.spinnerSupplier)
    Spinner spinnerSupplier;

    @ViewById(R.id.costPrice)
    EditText costPrice;

    @ViewById(R.id.retailPrice)
    EditText retailPrice;

    @ViewById(R.id.tax1)
    EditText tax1;

    @ViewById(R.id.tax1Percent)
    EditText tax1Percent;

    @ViewById(R.id.tax2)
    EditText tax2;

    @ViewById(R.id.tax2Percent)
    EditText tax2Percent;

    @ViewById(R.id.quantityStock)
    EditText quantityStock;

    @ViewById(R.id.receivingQuantity)
    EditText receivingQuantity;

    @ViewById(R.id.reorderLevel)
    EditText reorderLevel;

    @ViewById(R.id.description)
    EditText description;

    @ViewById(R.id.avatarImageView)
    ImageView avatarImageView;

    @ViewById(R.id.selectButton)
    Button selectButton;

    @ViewById(R.id.altCheckBox)
    CheckBox altCheckBox;

    @ViewById(R.id.serialCheckBox)
    CheckBox serialCheckBox;

    @ViewById(R.id.deletedCheckBox)
    CheckBox deletedCheckBox;

    @ViewById(R.id.newButton)
    Button newButton;

    @ViewById(R.id.submitButton)
    Button submitButton;

    @ViewById(R.id.viewGif)
    PlayGifView playGifView;

    String itemNameInput = "", categoryInput = "",costPriceInput = "",retailPriceInput = "",spinnerSupplierData="",
            quantityStockInput = "",receivingQuantityInput = "",reorderLevelInput = "",imagePath = "";

    boolean isNetworkAvailable = false;

    TextWatcher textDescription;

    String pageName = "";

    Activity activity;

    private ProgressDialog dialog;
    Bitmap bitmap;

    @AfterViews
    public void onInitView() {

        activity = AddItemActivity.this;

        Intent callingIntent=getIntent();

        pageName = callingIntent.getStringExtra("PageName");

        setActionBarSupport();

        checkInternetStatus();
        NetworkUtil.setOnNetworkChangeListener(this);
        submitButton.setOnClickListener(this);
        selectButton.setOnClickListener(this);
        newButton.setOnClickListener(this);

        altCheckBox.setChecked(false);
        serialCheckBox.setChecked(false);
        deletedCheckBox.setChecked(false);

        altCheckBox.setOnCheckedChangeListener(this);
        serialCheckBox.setOnCheckedChangeListener(this);
        deletedCheckBox.setOnCheckedChangeListener(this);

        spinnerSupplier.setOnItemSelectedListener(this);

        setView();
    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(pageName.equals(" New Sales Item")){
            getSupportActionBar().setTitle("New Item");
        }else {
            getSupportActionBar().setTitle(pageName);
        }


    }


    private void setView(){

        textDescription = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // you can check for enter key here
            }

            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() >= 150) {
                    VTools.showAlertDialog(activity,"Character Exceed the Limit");
                }
            }
        };

        new ActionEditText(this);
        description.addTextChangedListener(textDescription);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.submitButton:
                getInputs();
                validateInput();

                break;

            case R.id.newButton:
                getInputs();
                validateInputAndAddNew();
                break;

            case R.id.selectButton:
                showFileChooser();
                break;
        }
    }


    @FocusChange({R.id.UPC, R.id.itemName,R.id.category, R.id.costPrice,R.id.retailPrice, R.id.tax1,
            R.id.tax1Percent, R.id.tax2,R.id.tax2Percent, R.id.quantityStock,R.id.receivingQuantity,
            R.id.reorderLevel,R.id.description})
    public void focusChangedOnUser(View v, boolean hasFocus) {
        if (hasFocus) {
            textError.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (LogFlag.bLogOn) Log.d(TAG, "Back Pressed ");
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    void getInputs(){

        UPC.getText().toString();
        tax1.getText().toString();
        tax1Percent.getText().toString();
        tax2.getText().toString();
        tax2Percent.getText().toString();
        description.getText().toString();
        spinnerSupplierData = spinnerSupplier.getSelectedItem().toString();

    }

    void clearAllInputs(){

        UPC.setText("");
        tax1.setText("");
        tax1Percent.setText("");
        tax2.setText("");
        tax2Percent.setText("");
        description.setText("");
        itemName.setText("");
        category.setText("");
        costPrice.setText("");
        retailPrice.setText("");
        quantityStock.setText("");
        receivingQuantity.setText("");
        reorderLevel.setText("");
        altCheckBox.setChecked(false);
        serialCheckBox.setChecked(false);
        deletedCheckBox.setChecked(false);

    }

    void validateInput(){

        if(isNetworkAvailable){

            itemNameInput = itemName.getText().toString();
            categoryInput = category.getText().toString();
            costPriceInput = costPrice.getText().toString();
            retailPriceInput = retailPrice.getText().toString();
            quantityStockInput = quantityStock.getText().toString();
            receivingQuantityInput = receivingQuantity.getText().toString();
            reorderLevelInput = reorderLevel.getText().toString();


            if (!itemNameInput.isEmpty()&& !categoryInput.isEmpty() &&!costPriceInput.isEmpty() &&
                    !retailPriceInput.isEmpty()&&!quantityStockInput.isEmpty()&&
                    !receivingQuantityInput.isEmpty() &&!reorderLevelInput.isEmpty()) {

                playGifView.setVisibility(View.VISIBLE);
                textError.setVisibility(View.GONE);

                // TODO Service call
                if(pageName.equals("New Sales Customer")){
                    gotoSalesView();
                }else {
                    gotoItemView();
                }

            } else {

                setErrorMessage("Fill all Required Input");
            }

        }else {


            setErrorMessage("Check Network Connection");
        }
    }

    void validateInputAndAddNew(){

        if(isNetworkAvailable){

            itemNameInput = itemName.getText().toString();
            categoryInput = category.getText().toString();
            costPriceInput = costPrice.getText().toString();
            retailPriceInput = retailPrice.getText().toString();
            quantityStockInput = quantityStock.getText().toString();
            receivingQuantityInput = receivingQuantity.getText().toString();
            reorderLevelInput = reorderLevel.getText().toString();


            if (!itemNameInput.isEmpty()&& !categoryInput.isEmpty() &&!costPriceInput.isEmpty() &&
                    !retailPriceInput.isEmpty()&&!quantityStockInput.isEmpty()&&
                    !receivingQuantityInput.isEmpty() &&!reorderLevelInput.isEmpty()) {


                playGifView.setVisibility(View.VISIBLE);
                textError.setVisibility(View.GONE);
                
                clearAllInputs();
                // TODO Service call

            } else {
                setErrorMessage("Fill all Required Input");
            }

        }else {

            setErrorMessage("Check Network Connection");
        }
    }

    void setErrorMessage(String errorMessage) {
        playGifView.setVisibility(View.GONE);
        textError.setVisibility(View.VISIBLE);
        textError.setText(errorMessage);
    }

    @Override
    public void onChange(String status) {
        if (LogFlag.bLogOn)Log.d(TAG, "Network Availability: "+status);
        switch (status) {
            case "Connected to Internet with Mobile Data":
                isNetworkAvailable = true;
                break;
            case "Connected to Internet with WIFI":
                isNetworkAvailable = true;
                break;
            default:
                isNetworkAvailable = false;
                break;
        }
        if (LogFlag.bLogOn)Log.d(TAG, "isNetworkAvailable: "+isNetworkAvailable);
    }


    public  void checkInternetStatus(){
        String status = NetworkUtil.getConnectivityStatusString(getApplicationContext());
        switch (status) {
            case "Connected to Internet with Mobile Data":
                isNetworkAvailable = true;
                break;
            case "Connected to Internet with WIFI":
                isNetworkAvailable = true;
                break;
            default:
                isNetworkAvailable = false;
                break;
        }
        if (LogFlag.bLogOn)Log.d(TAG, "isNetworkAvailable: "+isNetworkAvailable);

    }

    @UiThread
    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), AppConstant.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            //Getting the Bitmap from Gallery
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                if (LogFlag.bLogOn) Log.e(TAG, e.toString());
            }

            if(null != filePath){
                if (LogFlag.bLogOn) Log.d(TAG,"FilePath: "+filePath);
                imagePath = filePath.getPath();
                if (LogFlag.bLogOn) Log.d(TAG,"ImageTaken: "+imagePath);
                Picasso.with(this).load(imagePath).into(avatarImageView);

            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.altCheckBox:
                if(isChecked){
                    altCheckBox.setChecked(true);
                }else {
                    altCheckBox.setChecked(false);
                }
                break;

            case R.id.serialCheckBox:
                if(isChecked){
                    serialCheckBox.setChecked(true);
                }else {
                    serialCheckBox.setChecked(false);
                }

                break;

            case R.id.deletedCheckBox:
                if(isChecked){
                    deletedCheckBox.setChecked(true);
                }else {
                    deletedCheckBox.setChecked(false);
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerSupplierData = spinnerSupplier.getSelectedItem().toString();
        if (LogFlag.bLogOn)Log.d(TAG, "spinnerSupplierData: " + spinnerSupplierData);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void gotoItemView(){
        Intent intent = new Intent(getApplicationContext(), ItemActivity_.class);
        startActivity(intent);
        finish();
    }
    private void gotoSalesView(){
        Intent intent = new Intent(getApplicationContext(), SalesActivity_.class);
        startActivity(intent);
        finish();
    }



   /* @UiThread
    public void startUploadImage(){
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Uploading...");
        dialog.show();
        uploadImageTask();
    }

    @Background
    public void uploadImageTask(){
         String webAddressToPost = "";

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext((HttpContext) getApplicationContext());
            HttpPost httpPost = new HttpPost(webAddressToPost);

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] data = bos.toByteArray();
            String file = Base64.encodeToString(data,0,0,0);
            entity.addPart("uploaded", new StringBody(file));

            entity.addPart("someOtherStringToSend", new StringBody("your string here"));

            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost,
                    localContext);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            response.getEntity().getContent(), "UTF-8"));

            String sResponse = reader.readLine();
            uploadImageTaskFinish(sResponse);
            if (LogFlag.bLogOn)Log.d(TAG, "sResponse: " + sResponse);
        } catch (Exception e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.toString());
        }
    }

    @UiThread
    public void uploadImageTaskFinish(String uploadResponse){
        if(uploadResponse != null){
            dialog.dismiss();
            Toast.makeText(getApplicationContext(), uploadResponse, Toast.LENGTH_LONG).show();
        }

    }*/
}