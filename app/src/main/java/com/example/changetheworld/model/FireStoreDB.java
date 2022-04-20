package com.example.changetheworld.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.changetheworld.AdapterSearch;
import com.example.changetheworld.AdapterTransaction;
import com.example.changetheworld.AdapterWallet;
import com.example.changetheworld.EditClientProfileActivity;
import com.example.changetheworld.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class FireStoreDB implements DataBaseInterface {

    private static FireStoreDB single_instance = null;
    public FirebaseFirestore db;

    DecimalFormat df = new DecimalFormat("0.00");
    public LocationDataApiInterface locationDataApi = new LocationDataApi();


    public Map<String,String> currenciesToSymbol = new HashMap<String, String>() {{
        put("USD", "$");
        put("EUR", "€");
        put("GBP", "£");
        put("CNY", "¥");
        put("ILS", "₪");
    }};

    public Map<String,String> symbolToCurrency = new HashMap<String, String>() {{
        put("$", "USD");
        put("€", "EUR");
        put("£", "GBP");
        put("¥", "CNY");
        put("₪", "ILS");
    }};

    public Map<String, String> stateToCurrency = new HashMap<String, String>() {{
        put("England", "GBP");
        put("United States", "USD");
        put("China", "CNY");
        put("Italy", "EUR");
        put("Israel", "ILS");
    }};

    private FireStoreDB() {
        db = FirebaseFirestore.getInstance();
    }

    public static FireStoreDB getInstance() {
        if (single_instance == null) {
            single_instance = new FireStoreDB();
        }
        return single_instance;
    }

    private void createDefaultWallet(String userName, String localCurrency, String type, Context context, Intent intent) {

        List<Task<Void>> tasks = new ArrayList<>();

        for (String c : currenciesToSymbol.keySet()) {

            Map<String, Object> walletData = new HashMap<>();
            walletData.put("currency", c);
            walletData.put("user_name", userName);
            walletData.put("balance", "0");
            walletData.put("symbol",  currenciesToSymbol.get(c));
            walletData.put("symbolLocalCurrency", currenciesToSymbol.get(localCurrency));
            walletData.put("localCurrency", localCurrency);

            Task<Void> task_wallet = db.collection(type)
                    .document(userName)
                    .collection("Wallet")
                    .document(c)
                    .set(walletData);

            tasks.add(task_wallet);
        }

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {
            intent.putExtra("userName", userName);
            context.startActivity(intent);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Fail create wallets : " + e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void VerifyAndSavePrivateClient(Context context, PrivateClient user, Intent intent) {
        Task<DocumentSnapshot> task_client = db.collection("PrivateClient")
                .document(user.getUser_name())
                .get();

        Task<DocumentSnapshot> task_business = db.collection("BusinessClient")
                .document(user.getUser_name())
                .get();

        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        tasks.add(task_client);
        tasks.add(task_business);

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {
            DocumentSnapshot client = (DocumentSnapshot) objects.get(0);
            DocumentSnapshot business = (DocumentSnapshot) objects.get(1);
            if (!client.exists() && !business.exists()) {
                SavePrivateClient(context, user, intent);
            } else {
                Toast.makeText(context, "UserName already exists", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Fail to connect to databases " + e.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void SavePrivateClient(Context context, PrivateClient user, Intent intent) {

        final String KEY_FULL_NAME = "full_name";
        final String KEY_PHONE = "phone";
        final String KEY_MAIL = "mail";
        final String KEY_CURRENCY = "currency";
        final String KEY_USER_NAME = "user_name";
        final String KEY_PASSWORD = "password";
        final String KEY_PERSONAL_PHOTO = "personal_photo";
        final String KEY_PASSPORT_PHOTO = "passport_photo";

        Map<String, Object> data = new HashMap<>();
        data.put(KEY_FULL_NAME, user.getFull_name());
        data.put(KEY_PHONE, user.getPhone_number());
        data.put(KEY_CURRENCY, user.getLocal_currency());
        data.put(KEY_USER_NAME, user.getUser_name());
        data.put(KEY_PASSWORD, user.getPassword());
        data.put(KEY_MAIL, user.getMail_address());

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference personalImageRef = storageRef.child("images/" + user.getUser_name() + "/" + KEY_PERSONAL_PHOTO + ".jpg");
        StorageReference passportImageRef = storageRef.child("images/" + user.getUser_name() + "/" + KEY_PASSPORT_PHOTO + ".jpg");
        UploadTask personal_photo_task = personalImageRef.putBytes(user.getPhoto());
        personal_photo_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());


        UploadTask passport_task = passportImageRef.putBytes(user.getPassport());
        passport_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());

        db.collection("PrivateClient")
                .document(user.getUser_name())
                .set(data)
                .addOnSuccessListener(unused -> Toast.makeText(context, "Client created successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Fail create new client : " + e.toString(), Toast.LENGTH_LONG).show());

        createDefaultWallet(user.getUser_name(), user.getLocal_currency(), "PrivateClient", context, intent);
    }

    @Override
    public void VerifyAndSaveBusiness(Context context, BusinessClient businessClient, Intent intent) {
        Task<DocumentSnapshot> task_client = db.collection("PrivateClient")
                .document(businessClient.getUser_name())
                .get();

        Task<DocumentSnapshot> task_business = db.collection("BusinessClient")
                .document(businessClient.getUser_name())
                .get();

        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        tasks.add(task_client);
        tasks.add(task_business);

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {
            DocumentSnapshot client = (DocumentSnapshot) objects.get(0);
            DocumentSnapshot business = (DocumentSnapshot) objects.get(1);
            if (!client.exists() && !business.exists()) {
                SaveBusinessClient(context, businessClient, intent);
            } else {
                Toast.makeText(context, "UserName already exists", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(context, "Fail to connect to databases " + e.toString(), Toast.LENGTH_LONG).show());

    }


    @Override
    public void VerifyAndLogin(Context context, String user_name, String password, Intent intent, String type) {
        db.collection(type)
                .document(user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        if (password.equals(documentSnapshot.getString("password")) && user_name.equals(documentSnapshot.getString("user_name"))) {
                            intent.putExtra("userName", user_name);
                            if (type.equals("PrivateClient"))
                                intent.putExtra("Local Currency", documentSnapshot.getString("currency"));
                            context.startActivity(intent);
                        }
                        else {
                            Toast.makeText(context, "UserName or password are incorrect", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(context, "UserName or password are incorrect", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(context, "Fail to connect to databases " + e.toString(), Toast.LENGTH_LONG).show());
    }

    @Override
    public void LoadProfilePhoto(ImageView profilPhoto, String user_name) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String personal_photo_path = "images/" + user_name + "/" + "personal_photo.jpg";
        StorageReference PersonalpathReference = storageRef.child(personal_photo_path);
        Task<byte[]> personal_photo = PersonalpathReference.getBytes(2000000000);
        personal_photo.addOnSuccessListener(bytes -> {
            Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            profilPhoto.setImageBitmap(photo);
        });
    }

    @Override
    public void LoadWallets(Context context, String user_name, String user_type, ArrayList<Wallet> items, RecyclerView recyclerView, TextView totalBalance, TextView symbol) {
        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        for (String currency: currenciesToSymbol.keySet()) {
            Task<DocumentSnapshot> tmp_wallet = db.collection(user_type).document(user_name).collection("Wallet").document(currency).get();
            tasks.add(tmp_wallet);
        }
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {

            AtomicReference<Float> sum = new AtomicReference<>((float) 0);
            AtomicReference<String> local_currencey_value = new AtomicReference<>();
            List<DocumentSnapshot> walletsData = new ArrayList<>();
            for (Object obj : objects) {
                DocumentSnapshot tmp_wallet = (DocumentSnapshot) obj;
                walletsData.add(tmp_wallet);
            }
            CurrencyDataApiInterface api = new CurrencyDataApi();
            HashMap<String, String> doc_data = new HashMap<>();
            ArrayList<String> symbols_for_api = new ArrayList<>();
            String local_currency = "";
            String user_name1 = "";
            for (DocumentSnapshot data : walletsData) {
                String balance = data.getString("balance");
                String currency = data.getString("currency");
                local_currency = data.getString("localCurrency");
                user_name1 = data.getString("user_name");
                doc_data.put(currency, balance);
                if(!currency.equals(local_currency))
                    symbols_for_api.add((currency + '/' + local_currency));
            }

            String finalUser_name = user_name1;
            String finalLocal_currency = local_currency;



            Thread t = new Thread(() -> {

                HashMap<String, ArrayList<Float>> price = api.getCloseAndChangePrice(symbols_for_api);

                for (String c : price.keySet()) {
                    float val = price.get(c).get(0);
                    local_currencey_value.set(df.format(val * Float.parseFloat(doc_data.get(c))));
                    Wallet walletData = new Wallet(df.format(Float.parseFloat(doc_data.get(c))), c, finalUser_name, currenciesToSymbol.get(c), local_currencey_value.get(), currenciesToSymbol.get(finalLocal_currency));
                    ((Activity) context).runOnUiThread(() -> {
                        items.add(walletData);
                        sum.updateAndGet(v -> new Float((float) (v + Float.parseFloat(walletData.getValueLocalCurrency()))));
                    });
                }

                ((Activity) context).runOnUiThread(() -> {
                    items.add(new Wallet(doc_data.get(finalLocal_currency),finalLocal_currency,finalUser_name,currenciesToSymbol.get(finalLocal_currency),doc_data.get(finalLocal_currency),currenciesToSymbol.get(finalLocal_currency)));
                    sum.updateAndGet(v -> new Float((float) (v + Float.parseFloat(doc_data.get(finalLocal_currency)))));
                    AdapterWallet adapterWallet = new AdapterWallet(context, items);
                    recyclerView.setAdapter(adapterWallet);
                    totalBalance.setText(String.valueOf(df.format(sum.get())));
                    symbol.setText(currenciesToSymbol.get(finalLocal_currency));
                });
            });
            t.start();
        });
    }



    @Override
    public void updateBalance(String user_name, String user_type, String wallet_name, float amount_in_foreign_currency, String action, Context context, Intent intent) {
        db.collection(user_type)
                .document(user_name)
                .collection("Wallet")
                .document(wallet_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    float new_balance;
                    if (action.equals("+"))
                        new_balance = Float.parseFloat(documentSnapshot.getString("balance")) + amount_in_foreign_currency;
                    else{
                        new_balance = Float.parseFloat(documentSnapshot.getString("balance")) - amount_in_foreign_currency;
                        if(new_balance < 0){
                            Toast.makeText(context, "Cannot Complete Action, Balance below 0",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    Map<String, Object> data = new HashMap<>();
                    data.put("balance", String.valueOf(new_balance));
                    db.collection(user_type)
                            .document(user_name)
                            .collection("Wallet")
                            .document(wallet_name)
                            .update(data)
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Cannot update balance on wallet",Toast.LENGTH_LONG).show();
                            }).addOnSuccessListener(unused -> {
                                Map<String, Object> transactionData = new HashMap<>();
                                transactionData.put("date", LocalDateTime.now().toString());
                                transactionData.put("amount", String.valueOf(amount_in_foreign_currency));
                                transactionData.put("action", action.equals("+") ? "deposit" : "withdraw");

                                db.collection(user_type)
                                        .document(user_name)
                                        .collection("Wallet")
                                        .document(wallet_name)
                                        .collection("Transactions")
                                        .document()
                                        .set(transactionData)
                                        .addOnSuccessListener(unused1 -> {
                                            Toast.makeText(context, action.equals("+") ? "deposit" : "withdraw" + " succeeded", Toast.LENGTH_LONG).show();
                                            context.startActivity(intent);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(context, "Cannot add transaction",Toast.LENGTH_LONG).show();
                                        });
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Cannot load wallet",Toast.LENGTH_LONG).show();
                });
    }


    @Override
    public void loadWalletHistory(String user_name, String user_type, String wallet_name, RecyclerView recyclerView, Context context) {
        ArrayList<Transaction> items = new ArrayList<>();
        db.collection(user_type)
                .document(user_name)
                .collection("Wallet")
                .document(wallet_name)
                .collection("Transactions")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> transactions = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot doc: transactions) {
                        Transaction tmp = new Transaction(doc.getString("amount"), doc.getString("date").replace("T", " ").split("\\.")[0], doc.getString("action"));
                        items.add(tmp);
                    }
                    Collections.sort(items);
                    AdapterTransaction adapterSubWallet = new AdapterTransaction(context, items);
                    recyclerView.setAdapter(adapterSubWallet);
                });

    }

    private void SaveBusinessClient(Context context, BusinessClient user, Intent intent) {
        final String KEY_BUSINESS_NAME = "business_name";
        final String KEY_MAIL = "mail";
        final String KEY_PHONE = "phone";
        final String KEY_USER_NAME = "user_name";
        final String KEY_PASSWORD = "password";
        final String KEY_OWNER_NAME = "owner_name";
        final String KEY_BUSINESS_APPROVAL = "business_approval";
        final String KEY_OWNER_ID = "owner_id";
        final String KEY_STATE = "state";
        final String KEY_CITY = "city";
        final String KEY_STREET = "street";
        final String KEY_NUMBER = "number";


        Map<String, Object> data = new HashMap<>();
        data.put(KEY_BUSINESS_NAME, user.getBusiness_name());
        data.put(KEY_MAIL, user.getMail_address());
        data.put(KEY_PHONE, user.getPhone());
        data.put(KEY_USER_NAME, user.getUser_name());
        data.put(KEY_PASSWORD, user.getPassword());
        data.put(KEY_OWNER_NAME, user.getBusiness_owner_name());
        data.put(KEY_STATE, user.getBusiness_state());
        data.put(KEY_CITY, user.getBusiness_city());
        data.put(KEY_STREET, user.getBusiness_street());
        data.put(KEY_NUMBER, user.getBusiness_no());


        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference personalImageRef = storageRef.child("images/" + user.getUser_name() + "/" + KEY_BUSINESS_APPROVAL + ".jpg");
        StorageReference passportImageRef = storageRef.child("images/" + user.getUser_name() + "/" + KEY_OWNER_ID + ".jpg");
        UploadTask business_approval_task = personalImageRef.putBytes(user.getBusiness_approval_document());
        business_approval_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());


        UploadTask owner_id_task = passportImageRef.putBytes(user.getGetBusiness_owner_id());
        owner_id_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());


        db.collection("BusinessClient")
                .document(user.getUser_name())
                .set(data)
                .addOnSuccessListener(unused -> Toast.makeText(context, "Business created successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Fail create new business : " + e.toString(), Toast.LENGTH_LONG).show());

        createDefaultWallet(user.getUser_name(), stateToCurrency.get(user.getBusiness_state()), "BusinessClient", context, intent);

    }

    @Override
    public void searchChange(String state, String city, String street, String number, RecyclerView recyclerView, Context context, ProgressBar progressBar) {
        db.collection("BusinessClient")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> business = queryDocumentSnapshots.getDocuments();
                    ArrayList<Search> searchBusinessClients = new ArrayList<>();
                    for (DocumentSnapshot b: business) {
                        Search tmp = new Search(b.getString("user_name"), b.getString("business_name"), "5", "open", b.getString("state"), b.getString("city"), b.getString("street"),  b.getString("number"));
                        searchBusinessClients.add(tmp);
                    }
                    String chosenAddress = state + " " + city + " " + street + " " + number;
                    Thread t = new Thread(() -> {
                        AtomicInteger flag = new AtomicInteger();
                        searchBusinessClients.sort((businessClient1, businessClient2) -> {
                            String business_address1 = businessClient1.getBusiness_state() + " " + businessClient1.getBusiness_city() + " " + businessClient1.getBusiness_street() + " " + businessClient1.getBusiness_no();
                            Float dis1 = locationDataApi.GetDistance(chosenAddress, business_address1);
                            String business_address2 = businessClient2.getBusiness_state() + " " + businessClient2.getBusiness_city() + " " + businessClient2.getBusiness_street() + " " + businessClient2.getBusiness_no();
                            Float dis2 = locationDataApi.GetDistance(chosenAddress, business_address2);
                            if (dis1 == null || dis2 == null){
                                flag.set(1);
                            }
                            else if (dis1 > dis2){
                                businessClient1.setDistance(String.valueOf(df.format(dis1)));
                                businessClient2.setDistance(String.valueOf(df.format(dis2)));
                                return 1;
                            }
                            else{
                                businessClient1.setDistance(String.valueOf(df.format(dis1)));
                                businessClient2.setDistance(String.valueOf(df.format(dis2)));
                                return -1;
                            }
                            return -1;
                        });
                        if (flag.get() == 1) {
                            ((Activity) context).runOnUiThread(() -> {
                                TextView errorLabel = ((Activity) context).findViewById(R.id.errorLabel);
                                errorLabel.setText("invalid address");
                                progressBar.setVisibility(View.INVISIBLE);
                            });
                            return;
                        }
                        ((Activity) context).runOnUiThread(() -> {
                            AdapterSearch adapterSearch = new AdapterSearch(context, searchBusinessClients);
                            recyclerView.setAdapter(adapterSearch);
                            progressBar.setVisibility(View.INVISIBLE);
                        });
                    });
                    t.start();
                });
    }

    @Override
    public void loadClientData(String user_name, TextView full_name, TextView mail_address, TextView phone_number, TextView local_currency) {
        db.collection("PrivateClient")
        .document(user_name)
        .get()
        .addOnSuccessListener(documentSnapshot -> {
            full_name.setText(documentSnapshot.getString("full_name"));
            mail_address.setText(documentSnapshot.getString("mail"));
            phone_number.setText(documentSnapshot.getString("phone"));
            local_currency.setText(documentSnapshot.getString("currency"));
        });
    }

    @Override
    public void loadClientDataForEdit(String user_name, EditText full_name, EditText mail_address, EditText phone_number, Spinner local_currency, EditText password)  {
        db.collection("PrivateClient")
                .document(user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    full_name.setText(documentSnapshot.getString("full_name"));
                    mail_address.setText(documentSnapshot.getString("mail"));
                    phone_number.setText(documentSnapshot.getString("phone"));
                    password.setText(documentSnapshot.getString("password"));
                    ArrayList<String> currencies = new ArrayList<>();
                    currencies.addAll(FireStoreDB.getInstance().currenciesToSymbol.keySet());
                    local_currency.setSelection(currencies.indexOf(documentSnapshot.getString("currency")));
                });
    }

    @Override
    public void updateClientProfile(Context context, PrivateClient client, Intent intent) {
        final String KEY_FULL_NAME = "full_name";
        final String KEY_PHONE = "phone";
        final String KEY_MAIL = "mail";
        final String KEY_CURRENCY = "currency";
        final String KEY_PASSWORD = "password";
        final String KEY_PERSONAL_PHOTO = "personal_photo";
        final String KEY_PASSPORT_PHOTO = "passport_photo";

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference personalImageRef = storageRef.child("images/" + client.getUser_name() + "/" + KEY_PERSONAL_PHOTO + ".jpg");
        UploadTask personal_photo_task = personalImageRef.putBytes(client.getPhoto());
        personal_photo_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());
//        StorageReference passportImageRef = storageRef.child("images/" + client.getUser_name() + "/" + KEY_PASSPORT_PHOTO + ".jpg");
//        UploadTask passport_task = passportImageRef.putBytes(client.getPassport());
//        passport_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());

        Map<String, Object> data = new HashMap<>();
        data.put(KEY_FULL_NAME, client.getFull_name());
        data.put(KEY_PHONE, client.getPhone_number());
        data.put(KEY_CURRENCY, client.getLocal_currency());
        data.put(KEY_PASSWORD, client.getPassword());
        data.put(KEY_MAIL, client.getMail_address());

        db.collection("PrivateClient")
                .document(client.getUser_name())
                .update(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Client updated successfully", Toast.LENGTH_SHORT).show();
                    intent.putExtra("userName", client.getUser_name());
                    context.startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Fail update new client : " + e.toString(), Toast.LENGTH_LONG).show());

    }

    @Override
    public void loadBusinessData(String user_name, TextView business_name, TextView mail_address, TextView phone_number, TextView owner_name, TextView state, TextView city, TextView street, TextView number) {
        db.collection("BusinessClient")
                .document(user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    business_name.setText(documentSnapshot.getString("business_name"));
                    mail_address.setText(documentSnapshot.getString("mail"));
                    phone_number.setText(documentSnapshot.getString("phone"));
                    owner_name.setText(documentSnapshot.getString("owner_name"));
                    state.setText(documentSnapshot.getString("state"));
                    city.setText(documentSnapshot.getString("city"));
                    street.setText(documentSnapshot.getString("street"));
                    number.setText(documentSnapshot.getString("number"));
                });
    }

    @Override
    public void loadBusinessDataForEdit(String user_name, TextView business_name, TextView mail_address, TextView phone_number, TextView owner_name, Spinner state, TextView city, TextView street, TextView number, EditText password)  {
        db.collection("PrivateClient")
                .document(user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    business_name.setText(documentSnapshot.getString("business_name"));
                    mail_address.setText(documentSnapshot.getString("mail"));
                    phone_number.setText(documentSnapshot.getString("phone"));
                    owner_name.setText(documentSnapshot.getString("owner_name"));
                    city.setText(documentSnapshot.getString("city"));
                    street.setText(documentSnapshot.getString("street"));
                    number.setText(documentSnapshot.getString("number"));
                    password.setText(documentSnapshot.getString("password"));
                    ArrayList<String> states = new ArrayList<>();
                    states.addAll(FireStoreDB.getInstance().stateToCurrency.keySet());
                    state.setSelection(states.indexOf(documentSnapshot.getString("state")));
                });
    }

    @Override
    public void updateBusinessProfile(Context context, BusinessClient business, Intent intent) {
        final String KEY_BUSINESS_NAME = "business_name";
        final String KEY_MAIL = "mail";
        final String KEY_PHONE = "phone";
        final String KEY_PASSWORD = "password";
        final String KEY_OWNER_NAME = "owner_name";
//        final String KEY_BUSINESS_APPROVAL = "business_approval";
//        final String KEY_OWNER_ID = "owner_id";
        final String KEY_STATE = "state";
        final String KEY_CITY = "city";
        final String KEY_STREET = "street";
        final String KEY_NUMBER = "number";


        Map<String, Object> data = new HashMap<>();
        data.put(KEY_BUSINESS_NAME, business.getBusiness_name());
        data.put(KEY_MAIL, business.getMail_address());
        data.put(KEY_PHONE, business.getPhone());
        data.put(KEY_PASSWORD, business.getPassword());
        data.put(KEY_OWNER_NAME, business.getBusiness_owner_name());
        data.put(KEY_STATE, business.getBusiness_state());
        data.put(KEY_CITY, business.getBusiness_city());
        data.put(KEY_STREET, business.getBusiness_street());
        data.put(KEY_NUMBER, business.getBusiness_no());


//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//        StorageReference personalImageRef = storageRef.child("images/" + user.getUser_name() + "/" + KEY_BUSINESS_APPROVAL + ".jpg");
//        StorageReference passportImageRef = storageRef.child("images/" + user.getUser_name() + "/" + KEY_OWNER_ID + ".jpg");
//        UploadTask business_approval_task = personalImageRef.putBytes(user.getBusiness_approval_document());
//        business_approval_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());
//
//
//        UploadTask owner_id_task = passportImageRef.putBytes(user.getGetBusiness_owner_id());
//        owner_id_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());


        db.collection("BusinessClient")
                .document(business.getUser_name())
                .update(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "business updated successfully", Toast.LENGTH_SHORT).show();
                    intent.putExtra("userName", business.getUser_name());
                    context.startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Fail create new business : " + e.toString(), Toast.LENGTH_LONG).show());

    }

}

