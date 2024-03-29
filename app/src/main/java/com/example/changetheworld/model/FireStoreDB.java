package com.example.changetheworld.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.changetheworld.AdapterBusinessCurrencyRate;
import com.example.changetheworld.AdapterBusinessCurrencyRateForShow;
import com.example.changetheworld.AdapterCurrency;
import com.example.changetheworld.AdapterOrder;
import com.example.changetheworld.AdapterSearch;
import com.example.changetheworld.AdapterTransaction;
import com.example.changetheworld.AdapterWallet;
import com.example.changetheworld.BusinessProfileActivity;
import com.example.changetheworld.OrderConfirm;
import com.example.changetheworld.OrderPage;
import com.example.changetheworld.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class FireStoreDB implements DataBaseInterface {

    private static FireStoreDB single_instance = null;
    public FirebaseFirestore db;

    DecimalFormat df = new DecimalFormat("0.00");
    public LocationDataApiInterface locationDataApi = new LocationDataApi();
    public CurrencyDataApiInterface api = new CurrencyDataApi();



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

    public Map<Integer,String> indexToDay = new HashMap<Integer, String>() {{
        put(0, "Sunday");
        put(1, "MonThu");
        put(2, "Friday");
        put(3, "Saturday");
    }};

    final String KEY_BUSINESS_APPROVAL = "business_approval";
    final String KEY_OWNER_ID = "owner_id";
    final String KEY_PASSPORT_PHOTO = "passport_photo";

    private FireStoreDB() {
        db = FirebaseFirestore.getInstance();
    }

    public static FireStoreDB getInstance() {
        if (single_instance == null) {
            single_instance = new FireStoreDB();
        }
        return single_instance;
    }

    private void createDefaultWallet(String userName, String type, Context context, Intent intent) {

        List<Task<Void>> tasks = new ArrayList<>();

        for (String c : currenciesToSymbol.keySet()) {

            Map<String, Object> walletData = new HashMap<>();
            walletData.put("currency", c);
            walletData.put("user_name", userName);
            walletData.put("balance", "0");
            walletData.put("symbol",  currenciesToSymbol.get(c));

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
        }).addOnFailureListener(e -> Toast.makeText(context, "Fail create wallets : " + e.toString(), Toast.LENGTH_LONG).show());
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
        }).addOnFailureListener(e -> Toast.makeText(context, "Fail to connect to databases " + e.toString(), Toast.LENGTH_LONG).show());
    }

    private void SavePrivateClient(Context context, PrivateClient user, Intent intent) {

        final String KEY_FULL_NAME = "full_name";
        final String KEY_PHONE = "phone";
        final String KEY_MAIL = "mail";
        final String KEY_CURRENCY = "local_currency";
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

        if (user.getPhoto() != null) {
            StorageReference personalImageRef = storageRef.child("images/" + user.getUser_name() + "/" + KEY_PERSONAL_PHOTO + ".jpg");
            UploadTask personal_photo_task = personalImageRef.putBytes(user.getPhoto());
            personal_photo_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());
        }

        if (user.getPassport() != null) {
            StorageReference passportImageRef = storageRef.child("images/" + user.getUser_name() + "/" + KEY_PASSPORT_PHOTO + ".jpg");
            UploadTask passport_task = passportImageRef.putBytes(user.getPassport());
            passport_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());
        }
        db.collection("PrivateClient")
                .document(user.getUser_name())
                .set(data)
                .addOnSuccessListener(unused -> Toast.makeText(context, "Client created successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Fail create new client : " + e.toString(), Toast.LENGTH_LONG).show());

        createDefaultWallet(user.getUser_name(), "PrivateClient", context, intent);
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

            db.collection(user_type)
                    .document(user_name)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String local_currency = documentSnapshot.getString("local_currency");

                        HashMap<String, String> doc_data = new HashMap<>();
                        ArrayList<String> symbols_for_api = new ArrayList<>();
                        String user_name1 = "";
                        for (DocumentSnapshot data : walletsData) {
                            String balance = data.getString("balance");
                            String currency = data.getString("currency");
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
                                //TODO: Fail here
                                items.add(new Wallet(df.format(Float.parseFloat(doc_data.get(finalLocal_currency))),finalLocal_currency,finalUser_name,currenciesToSymbol.get(finalLocal_currency),df.format(Float.parseFloat(doc_data.get(finalLocal_currency))),currenciesToSymbol.get(finalLocal_currency)));
                                sum.updateAndGet(v -> new Float((float) (v + Float.parseFloat(doc_data.get(finalLocal_currency)))));
                                AdapterWallet adapterWallet = new AdapterWallet(context, items);
                                recyclerView.setAdapter(adapterWallet);
                                totalBalance.setText(String.valueOf(df.format(sum.get())));
                                symbol.setText(currenciesToSymbol.get(finalLocal_currency));
                            });
                        });
                        t.start();
                    });
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
                            Toast.makeText(context, "Cannot complete action, balance below 0",Toast.LENGTH_LONG).show();
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
                                            if (intent != null) {
                                                intent.putExtra("subWalletBalance",df.format(new_balance));
                                                context.startActivity(intent);
                                            }
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
        final String KEY_ADDRESS = "address";
        final String KEY_LOCAL_CURRENCY = "local_currency";
        final String KEY_NUMBER_OF_TRADES = "number_of_trades";
        final String KEY_TOTAL_PROFIT = "total_profit";
        final String KEY_AVG_PROFIT = "avg_profit";

        Map<String, Object> data = new HashMap<>();
        data.put(KEY_BUSINESS_NAME, user.getBusiness_name());
        data.put(KEY_MAIL, user.getMail_address());
        data.put(KEY_PHONE, user.getPhone());
        data.put(KEY_USER_NAME, user.getUser_name());
        data.put(KEY_PASSWORD, user.getPassword());
        data.put(KEY_OWNER_NAME, user.getBusiness_owner_name());
        data.put(KEY_ADDRESS, user.getAddress());
        data.put(KEY_LOCAL_CURRENCY, user.getLocal_currency());
        data.put(KEY_NUMBER_OF_TRADES, "0");
        data.put(KEY_TOTAL_PROFIT, "0");
        data.put(KEY_AVG_PROFIT, "0");

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            StorageReference personalImageRef = storageRef.child("images/" + user.getUser_name() + "/" + KEY_BUSINESS_APPROVAL + ".jpg");
            UploadTask business_approval_task = personalImageRef.putBytes(user.getBusiness_approval_document());
            business_approval_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());


            StorageReference passportImageRef = storageRef.child("images/" + user.getUser_name() + "/" + KEY_OWNER_ID + ".jpg");
            UploadTask owner_id_task = passportImageRef.putBytes(user.getGetBusiness_owner_id());
            owner_id_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());

        db.collection("BusinessClient")
                .document(user.getUser_name())
                .set(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Business created successfully", Toast.LENGTH_SHORT).show();
                    HashMap<String, String> commission = new HashMap<String, String>();
                    for (String sym1: currenciesToSymbol.keySet()) {
                        for (String sym2: currenciesToSymbol.keySet()) {
                            if(!sym1.equals(sym2)){
                                commission.put(sym1 + "/" + sym2, "0");
                            }
                        }
                    }
                    saveChangeComissionRate(context, commission, user.getUser_name(), true);
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Fail create new business : " + e.toString(), Toast.LENGTH_LONG).show());
        createDefaultWallet(user.getUser_name(), "BusinessClient", context, intent);
    }

    @Override
    public void searchChange(String searchQuery, String radius, RecyclerView recyclerView, Context context, ProgressBar progressBar, ArrayList<Search> filter_list) {

        Format f = new SimpleDateFormat("HH:mm");
        String nowTime = f.format(new Date());
        f = new SimpleDateFormat("EEEE");
        String nowDay = f.format(new Date());

        if (nowDay.equals("Monday") || nowDay.equals("Tuesday") || nowDay.equals("Wednesday"));
            nowDay = "MonThu";

        HashMap <String, String> open_close_business = new HashMap<>();

        String finalNowDay = nowDay;

        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();

        Task getOpenHours = db.collection("BusinessClient")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> business = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot b : business) {
                                String user_name = b.getString("user_name");

                                db.collection("BusinessClient")
                                        .document(b.getString("user_name"))
                                        .collection("OpenHours")
                                        .document(finalNowDay)
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            try {
                                                String string1 = documentSnapshot.getString("open");
                                                String string2 = documentSnapshot.getString("close");
                                                if (string1 != null && string2 != null) {
                                                    Date open = new SimpleDateFormat("HH:mm").parse(string1);
                                                    Date close = new SimpleDateFormat("HH:mm").parse(string2);
                                                    Date now = new SimpleDateFormat("HH:mm").parse(nowTime);
                                                    int open_st = open.compareTo(now);
                                                    int close_st = close.compareTo(now);
                                                    if (open_st < 0 && close_st > 0)
                                                        open_close_business.put(user_name, "open");
                                                    else
                                                        open_close_business.put(user_name, "close");
                                                } else
                                                    open_close_business.put(user_name, "");
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                        });
                            }
                        });

        tasks.add(getOpenHours);

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {
            db.collection("BusinessClient")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                        List<DocumentSnapshot> business1 = queryDocumentSnapshots1.getDocuments();
                        ArrayList<Search> searchBusinessClients = new ArrayList<>();
                        for (int i = 0; i < business1.size(); i++) {
                            String rate = business1.get(i).getString("rate") != null ? business1.get(i).getString("rate") : "0";
                            Search tmp = new Search(business1.get(i).getString("user_name"), business1.get(i).getString("business_name"), rate,
                                    open_close_business.get(business1.get(i).getString("user_name")), business1.get(i).getString("address"));
                            searchBusinessClients.add(tmp);
                        }
                        Thread t = new Thread(() -> {
                            AtomicInteger flag = new AtomicInteger(0);
                            if (searchBusinessClients.size() == 1){
                                Float dis = locationDataApi.GetDistance(searchQuery, searchBusinessClients.get(0).getBusiness_address());
                                searchBusinessClients.get(0).setDistance(String.valueOf(df.format(dis)));
                            }
                            searchBusinessClients.sort((businessClient1, businessClient2) -> {
                                String business_address1 = businessClient1.getBusiness_address() ;
                                Float dis1 = locationDataApi.GetDistance(searchQuery, business_address1);
                                String business_address2 = businessClient2.getBusiness_address();
                                Float dis2 = locationDataApi.GetDistance(searchQuery, business_address2);
                                if (dis1 == null || dis2 == null) {
                                    flag.set(1);
                                } else if (dis1 > dis2) {
                                    businessClient1.setDistance(String.valueOf(df.format(dis1)));
                                    businessClient2.setDistance(String.valueOf(df.format(dis2)));
                                    return 1;
                                } else {
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

                                filter_list.addAll(searchBusinessClients.stream().filter(search -> {
                                    if (Float.valueOf(search.distance) <= Float.valueOf(radius)) {
                                        return true;
                                    }
                                    return false;
                                }).collect(Collectors.toList()));
                                AdapterSearch adapterSearch = new AdapterSearch(context, filter_list);
                                recyclerView.setAdapter(adapterSearch);
                                progressBar.setVisibility(View.INVISIBLE);
                            });
                        });
                        t.start();
                    });
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
            local_currency.setText(documentSnapshot.getString("local_currency"));
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
                    local_currency.setSelection(currencies.indexOf(documentSnapshot.getString("local_currency")));
                });
    }

    @Override
    public void updateClientProfile(Context context, PrivateClient client, Intent intent) {
        final String KEY_FULL_NAME = "full_name";
        final String KEY_PHONE = "phone";
        final String KEY_MAIL = "mail";
        final String KEY_CURRENCY = "local_currency";
        final String KEY_PASSWORD = "password";
        final String KEY_PERSONAL_PHOTO = "personal_photo";
        final String KEY_PASSPORT_PHOTO = "passport_photo";

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        if (client.getPhoto() != null) {
            StorageReference personalImageRef = storageRef.child("images/" + client.getUser_name() + "/" + KEY_PERSONAL_PHOTO + ".jpg");
            UploadTask personal_photo_task = personalImageRef.putBytes(client.getPhoto());
            personal_photo_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());
        }

        if (client.getPassport() != null) {
            StorageReference passportImageRef = storageRef.child("images/" + client.getUser_name() + "/" + KEY_PASSPORT_PHOTO + ".jpg");
            UploadTask passport_task = passportImageRef.putBytes(client.getPassport());
            passport_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());
        }

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
    public void loadBusinessData(String user_name, TextView header, TextView business_name, TextView mail_address, TextView phone_number, TextView owner_name, TextView address, TextView local_currency, TextView sundayHours, TextView monThuHours, TextView fridayHours, TextView saturdayHours) {
        db.collection("BusinessClient")
                .document(user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    business_name.setText(documentSnapshot.getString("business_name"));
                    mail_address.setText(documentSnapshot.getString("mail"));
                    phone_number.setText(documentSnapshot.getString("phone"));
                    if (header != null)
                        header.setText(documentSnapshot.getString("business_name"));
                    if (owner_name != null)
                        owner_name.setText(documentSnapshot.getString("owner_name"));
                    address.setText(documentSnapshot.getString("address"));
                    local_currency.setText(documentSnapshot.getString("local_currency"));
                    loadBusinessOpenHours(user_name, sundayHours, monThuHours, fridayHours, saturdayHours);
                });
    }

    public void loadBusinessOpenHours(String user_name, TextView sundayHours, TextView monThuHours, TextView fridayHours, TextView saturdayHours) {

        for (int i = 0; i < 4; i++) {
            int finalI = i;
            db.collection("BusinessClient")
                    .document(user_name)
                    .collection("OpenHours")
                    .document(indexToDay.get(i))
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String open = documentSnapshot.getString("open");
                        if (open == null)
                            open = "";
                        String close = documentSnapshot.getString("close");
                        if (close == null)
                            close = "";
                        switch(finalI) {
                            case 0:
                                sundayHours.setText(open + " - " + close);
                                break;
                            case 1:
                                monThuHours.setText(open + " - " + close);
                                break;
                            case 2:
                                fridayHours.setText(open + " - " + close);
                                break;
                            case 3:
                                saturdayHours.setText(open + " - " + close);
                                break;
                            default:
                        }
                    });
        };
    }

    @Override
    public void loadBusinessDataForEdit(String user_name, EditText business_name, EditText mail_address, EditText phone_number, EditText owner_name, Spinner local_currency, EditText address, EditText password, EditText sundayOpen, EditText sundayClose, EditText monThuOpen, EditText monThuClose, EditText fridayOpen, EditText fridayClose, EditText saturdayOpen, EditText saturdayClose )  {
        db.collection("BusinessClient")
                .document(user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    business_name.setText(documentSnapshot.getString("business_name"));
                    mail_address.setText(documentSnapshot.getString("mail"));
                    phone_number.setText(documentSnapshot.getString("phone"));
                    owner_name.setText(documentSnapshot.getString("owner_name"));
                    address.setText(documentSnapshot.getString("address"));
                    password.setText(documentSnapshot.getString("password"));
                    ArrayList<String> local_currencies = new ArrayList<>();
                    local_currencies.addAll(FireStoreDB.getInstance().currenciesToSymbol.keySet());
                    local_currency.setSelection(local_currencies.indexOf(documentSnapshot.getString("local_currency")));
                    loadBusinessOpenHoursForEdit(user_name, sundayOpen, sundayClose, monThuOpen, monThuClose,fridayOpen, fridayClose, saturdayOpen, saturdayClose);
                });
    }

    public void loadBusinessOpenHoursForEdit(String user_name, EditText sundayOpen, EditText sundayClose, EditText monThuOpen, EditText monThuClose,EditText fridayOpen, EditText fridayClose, EditText saturdayOpen, EditText saturdayClose) {

        for (int i = 0; i < 4; i++) {
            int finalI = i;
            db.collection("BusinessClient")
                    .document(user_name)
                    .collection("OpenHours")
                    .document(indexToDay.get(i))
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String open = documentSnapshot.getString("open");
                        String close = documentSnapshot.getString("close");
                        switch(finalI) {
                            case 0:
                                sundayOpen.setText(open);
                                sundayClose.setText(close);
                                break;
                            case 1:
                                monThuOpen.setText(open);
                                monThuClose.setText(close);
                                break;
                            case 2:
                                fridayOpen.setText(open);
                                fridayClose.setText(close);
                                break;
                            case 3:
                                saturdayOpen.setText(open);
                                saturdayClose.setText(close);
                                break;
                            default:
                        }
                    });
        };
    }

    @Override
    public void updateBusinessProfile(Context context, BusinessClient business, ArrayList<OpenHours> openHours, Intent intent) {
        final String KEY_BUSINESS_NAME = "business_name";
        final String KEY_MAIL = "mail";
        final String KEY_PHONE = "phone";
        final String KEY_PASSWORD = "password";
        final String KEY_OWNER_NAME = "owner_name";
        final String KEY_BUSINESS_APPROVAL = "business_approval";
        final String KEY_OWNER_ID = "owner_id";
        final String KEY_ADDRESS = "address";
        final String KEY_LOCAL_CURRENCY = "local_currency";


        Map<String, Object> data = new HashMap<>();
        data.put(KEY_BUSINESS_NAME, business.getBusiness_name());
        data.put(KEY_MAIL, business.getMail_address());
        data.put(KEY_PHONE, business.getPhone());
        data.put(KEY_PASSWORD, business.getPassword());
        data.put(KEY_OWNER_NAME, business.getBusiness_owner_name());
        data.put(KEY_ADDRESS, business.getAddress());
        data.put(KEY_LOCAL_CURRENCY, business.getLocal_currency());

        db.collection("BusinessClient")
                .document(business.getUser_name())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    String prev_local = documentSnapshot.getString("local_currency");
                    ArrayList<String> pair = new ArrayList<>();
                    pair.add(prev_local + "/" + business.getLocal_currency());
                    new Thread(()-> {
                        if(!prev_local.equals(business.getLocal_currency())) {
                            Float rate = api.getCloseAndChangePrice(pair).get(prev_local).get(0);
                            Float new_total = Float.parseFloat(documentSnapshot.getString("total_profit")) * rate;
                            Float new_avg = new_total / Integer.parseInt(documentSnapshot.getString("number_of_trades"));
                            data.put("total_profit", new_total);
                            data.put("avg_profit", new_avg);
                        }
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

                        if (business.getBusiness_approval_document() != null) {
                            StorageReference Business_approvalRef = storageRef.child("images/" + business.getUser_name() + "/" + KEY_BUSINESS_APPROVAL + ".jpg");
                            UploadTask Business_approval_task = Business_approvalRef.putBytes(business.getBusiness_approval_document());
                            Business_approval_task.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());
                        }

                        if (business.getGetBusiness_owner_id() != null) {
                            StorageReference owner_idRef = storageRef.child("images/" + business.getUser_name() + "/" + KEY_OWNER_ID + ".jpg");
                            UploadTask owner_idTask = owner_idRef.putBytes(business.getGetBusiness_owner_id());
                            owner_idTask.addOnFailureListener(e -> Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show());
                        }

                        db.collection("BusinessClient")
                                .document(business.getUser_name())
                                .update(data)
                                .addOnSuccessListener(unused -> {
                                    saveOpenHours(context, business.getUser_name(), openHours, intent);
                                })
                                .addOnFailureListener(e -> Toast.makeText(context, "Fail update business : " + e.toString(), Toast.LENGTH_LONG).show());
                    }).start();
                });

    }

    @Override
    public void loadCurrencyRates(Context context, String user_name, RecyclerView recyclerView, ProgressBar progressBar, ArrayList<business_currency_rate> bcrs) {
        ArrayList<String> pairs = new ArrayList<>();
        ArrayList<String> pair_symbols = new ArrayList<>();
        for (String s1: currenciesToSymbol.keySet()) {
            for (String s2: currenciesToSymbol.keySet()) {
                if (!s1.equals(s2)){
                    pairs.add(s1 + "/" + s2);
                    pair_symbols.add(currenciesToSymbol.get(s1) + "/" + currenciesToSymbol.get(s2));
                }
            }
        }
        Thread t = new Thread(() -> {
            HashMap<String, Float> prices = api.getAllPairPrices(pairs);
            for(int i = 0; i < pairs.size(); i++){
                bcrs.add(new business_currency_rate(pair_symbols.get(i), String.valueOf(prices.get(pairs.get(i))), pairs.get(i), String.valueOf(prices.get(pairs.get(i))), "0"));
            }
            ((Activity) context).runOnUiThread(() -> {
                AdapterBusinessCurrencyRate abcr = new AdapterBusinessCurrencyRate(context, bcrs);
                recyclerView.setAdapter(abcr);
                progressBar.setVisibility(View.INVISIBLE);


            });
        });
        t.start();
    }

    @Override
    public void loadCurrencyRatesForShow(Context context, String user_name, RecyclerView recyclerView, ProgressBar progressBar, ArrayList<BusinessCurrencyRateForShow> bcrs) {


        db.collection("BusinessClient")
                    .document(user_name)
                    .collection("currencyComission")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<DocumentSnapshot> currency_pairs = queryDocumentSnapshots.getDocuments();
                        if (currency_pairs.size() == 0) {
                            ArrayList<String> pairs = new ArrayList<>();
                            for (String s1: currenciesToSymbol.keySet()) {
                                for (String s2: currenciesToSymbol.keySet()) {
                                    if (!s1.equals(s2)){
                                        pairs.add(s1 + " " + s2);
                                    }
                                }
                            }

                            HashMap<Pair<String, String>, String> pairs1 = new HashMap<>();

                            for (int i = 0; i < pairs.size(); i++) {
                                Boolean exist = false;
                                String pair = pairs.get(i);
                                String s1 = pair.split(" ")[0];
                                String s2 = pair.split(" ")[1];
                                for (Pair<String, String> p: pairs1.keySet()) {
                                    if (p.first.equals(s2) && p.second.equals(s1))
                                        exist = true;
                                }
                                Pair<String, String> key = new Pair<>(s1, s2);
                                if (!exist) {
                                    pairs1.put(key, "0");
                                }
                            }

                            ArrayList<BusinessCurrencyRateForShow> sale_buy_prices = new ArrayList<>();
                            Thread t = new Thread(() -> {
                                for (Pair<String, String> p: pairs1.keySet()) {
                                    String symbolId = currenciesToSymbol.get(p.first) + " " + currenciesToSymbol.get(p.second);
                                    String currencyName = p.first + " " + p.second;
                                    ArrayList<String> pair = new ArrayList<>();
                                    pair.add(p.first + '/' + p.second);
                                    pair.add(p.second + '/' + p.first);

                                    String sale_price = df.format(api.getCloseAndChangePrice(pair).get(p.first).get(0));
                                    String buy_price =  df.format(api.getCloseAndChangePrice(pair).get(p.second).get(0));
                                    BusinessCurrencyRateForShow tmp = new BusinessCurrencyRateForShow(symbolId, currencyName,
                                            sale_price, buy_price);
                                    sale_buy_prices.add(tmp);
                                }
                                ((Activity) context).runOnUiThread(() -> {
                                    AdapterBusinessCurrencyRateForShow abcr = new AdapterBusinessCurrencyRateForShow(context, sale_buy_prices);
                                    recyclerView.setAdapter(abcr);
                                    progressBar.setVisibility(View.INVISIBLE);

                                });
                            });
                            t.start();

                        }

                        else {

                            HashMap<Pair<String, String>, String> pairs = new HashMap<>();
                            HashMap<String, String> allPairs = new HashMap<>();

                            for (int i = 0; i < currency_pairs.size(); i++) {
                                Boolean exist = false;
                                String pair = currency_pairs.get(i).getId();
                                String s1 = pair.split("\\*")[0];
                                String s2 = pair.split("\\*")[1];
                                for (Pair<String, String> p : pairs.keySet()) {
                                    if (p.first.equals(s2) && p.second.equals(s1))
                                        exist = true;
                                }
                                Pair<String, String> key = new Pair<>(s1, s2);
                                String value = currency_pairs.get(i).getString(pair.replace("*", ""));
                                if (!exist) {
                                    pairs.put(key, value);
                                }
                                allPairs.put(s1 + s2, value);
                            }
                            ArrayList<BusinessCurrencyRateForShow> sale_buy_prices = new ArrayList<>();
                            Thread t = new Thread(() -> {
                                for (Pair<String, String> p : pairs.keySet()) {
                                    String symbolId = currenciesToSymbol.get(p.first) + " " + currenciesToSymbol.get(p.second);
                                    String currencyName = p.first + " " + p.second;
                                    ArrayList<String> pair = new ArrayList<>();
                                    pair.add(p.first + '/' + p.second);
                                    pair.add(p.second + '/' + p.first);

                                    String sale_price = df.format(api.getCloseAndChangePrice(pair).get(p.first).get(0) - Float.parseFloat(pairs.get(p)));
                                    String buy_price = df.format(api.getCloseAndChangePrice(pair).get(p.second).get(0) - Float.parseFloat(allPairs.get(p.second + p.first)));
                                    BusinessCurrencyRateForShow tmp = new BusinessCurrencyRateForShow(symbolId, currencyName,
                                            sale_price, buy_price);
                                    sale_buy_prices.add(tmp);
                                }
                                ((Activity) context).runOnUiThread(() -> {
                                    AdapterBusinessCurrencyRateForShow abcr = new AdapterBusinessCurrencyRateForShow(context, sale_buy_prices);
                                    recyclerView.setAdapter(abcr);
                                    progressBar.setVisibility(View.INVISIBLE);

                                });
                            });
                            t.start();
                        }
                    });
    }

    @Override
    public void saveChangeComissionRate(Context context, HashMap<String, String> comission_data, String business_user_name, boolean new_rates) {

        ArrayList<Task> tasks = new ArrayList<>();

        for (String pair: comission_data.keySet()) {
            HashMap<String, Object> data = new HashMap<>();
            data.put(pair.replace("/",""), comission_data.get(pair));
            tasks.add(db.collection("BusinessClient")
                    .document(business_user_name)
                    .collection("currencyComission")
                    .document(pair.replace("/","*"))
                    .set(data)
            );

        }
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {
            if (!new_rates){
                Toast.makeText(context, "Rates updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, BusinessProfileActivity.class);
                intent.putExtra("userName", business_user_name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public void calculateChangeRate(Context context, String business_user_name, String from_currency, String to_currency, float amount, TextView receive, AtomicReference<Float> change_profit) {

        ArrayList<String> pair = new ArrayList<>();
        pair.add(from_currency + '/' + to_currency);
        db.collection("BusinessClient")
                .document(business_user_name)
                .collection("currencyComission")
                .document(from_currency + '*' + to_currency)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    float change_comission = 0 ;
                    if (documentSnapshot != null && documentSnapshot.getString(from_currency + to_currency) != null)
                        change_comission = Float.parseFloat(Objects.requireNonNull(documentSnapshot.getString(from_currency + to_currency)));
                    float finalChange_comission = change_comission;
                    Thread t = new Thread(()->{
                        Float current_price = api.getCloseAndChangePrice(pair).get(from_currency).get(0);
                        Float change_price = (current_price - finalChange_comission) * amount;
                        Float price_without_commission = current_price * amount;
                        change_profit.set(price_without_commission - change_price);
                        ArrayList<String> profit_pair = new ArrayList<>();
                        db.collection("BusinessClient")
                                .document(business_user_name)
                                .get()
                                .addOnSuccessListener(documentSnapshot1 -> {
                                    new Thread(()->{
                                        profit_pair.add(to_currency + "/" + documentSnapshot1.getString("local_currency"));
                                        float convert_local_currency_price = 1.0F;
                                        if (!to_currency.equals(documentSnapshot1.getString("local_currency"))){
                                            convert_local_currency_price = api.getCloseAndChangePrice(profit_pair).get(to_currency).get(0);
                                        }
                                        change_profit.set(change_profit.get() * convert_local_currency_price);
                                        ((Activity)context).runOnUiThread(()->{
                                            receive.setText(String.valueOf(df.format(change_price)));
                                        });
                                    }).start();
                                });
                    });
                    t.start();
                });
    }

    @Override
    public void LoadBusinessAddress(Context context, String business_user_name, TextView pick_from) {
        db.collection("BusinessClient")
                .document(business_user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    ((Activity)context).runOnUiThread(()->{
                        pick_from.setText(documentSnapshot.getString("address"));
                    });
                });
    }

    @Override
    public void PayByCash(Context context, String user_type, String business_user_name, String client_user_name, String from_currency, String to_currency, String from_amount, String to_amount, String date, String business_address, AtomicReference<Float> change_profit) {
        List<Task<DocumentSnapshot>> getDataTasks = new ArrayList<>();

        AtomicReference<String> business_name = new AtomicReference<>();
        AtomicReference<String> user_FullName = new AtomicReference<>();
        AtomicReference<String> counter = new AtomicReference<>();

        getDataTasks.add(db.collection("BusinessClient")
                .document(business_user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    business_name.set(documentSnapshot.getString("business_name"));
                }));

        getDataTasks.add(db.collection(user_type)
                .document(client_user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                        if (user_type.equals("PrivateClient"))
                                user_FullName.set(documentSnapshot.getString("full_name"));
                        else
                                user_FullName.set(documentSnapshot.getString("business_name"));
                }));

        getDataTasks.add(db.collection("CounterForUniqueID")
                .document("value")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    counter.set(documentSnapshot.getString("counter"));
                }));

        Tasks.whenAllSuccess(getDataTasks).addOnSuccessListener(objects -> {

            List<Task<Void>> tasks = new ArrayList<>();

            Map<String, Object> data = new HashMap<>();
            data.put("user_fullName", user_FullName.get());
            data.put("business_name", business_name.get());
            data.put("payment_method", "cash");
            data.put("from_currency", from_currency);
            data.put("to_currency", to_currency);
            data.put("from_amount", from_amount);
            data.put("to_amount", to_amount);
            data.put("date", date);
            data.put("status", "pending");
            data.put("id", client_user_name + "*" + business_user_name + "*" + counter.get());
            data.put("client_type", user_type);
            data.put("change_profit", String.valueOf(change_profit.get()));

            tasks.add(db.collection(user_type)
                    .document(client_user_name)
                    .collection("OrdersByMe")
                    .document(client_user_name + "*" + business_user_name + "*" + counter.get())
                    .set(data));

            tasks.add(db.collection("BusinessClient")
                    .document(business_user_name)
                    .collection("OrdersForMe")
                    .document(client_user_name + "*" + business_user_name + "*" + counter.get())
                    .set(data));

            Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects1 -> {
                Map<String, Object> updated_counter = new HashMap<>();
                updated_counter.put("counter",  String.valueOf(Integer.parseInt(counter.get()) + 1));
                db.collection("CounterForUniqueID")
                        .document("value")
                        .set(updated_counter)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(context, "Order Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, OrderConfirm.class);
                            intent.putExtra("orderID", client_user_name + "*" + business_user_name + "*" + counter.get());
                            intent.putExtra("user_type",user_type);
                            intent.putExtra("user_name",client_user_name);
                            intent.putExtra("status", "pending");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            context.startActivity(intent);
                        });
            });

        });
    }

    @Override
    public void PayByWallet(Context context, String user_type, String business_user_name, String client_user_name, String from_currency, String to_currency, String from_amount, String to_amount, String date, String business_address, AtomicReference<Float> change_profit) {
        db.collection(user_type)
                .document(client_user_name)
                .collection("Wallet")
                .document(from_currency)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    Float balance = Float.parseFloat(documentSnapshot.getString("balance"));
                    Float reduceBalance = Float.parseFloat(from_amount);
                    if (balance - reduceBalance >= 0) {
                        updateBalance(client_user_name, user_type, from_currency, reduceBalance, "-", context, null);

                        List<Task<DocumentSnapshot>> getDataTasks = new ArrayList<>();

                        AtomicReference<String> business_name = new AtomicReference<>();
                        AtomicReference<String> user_FullName = new AtomicReference<>();
                        AtomicReference<String> counter = new AtomicReference<>();

                        getDataTasks.add(db.collection("BusinessClient")
                                .document(business_user_name)
                                .get()
                                .addOnSuccessListener(documentSnapshot1 -> {
                                    business_name.set(documentSnapshot1.getString("business_name"));
                                }));

                        getDataTasks.add(db.collection(user_type)
                                .document(client_user_name)
                                .get()
                                .addOnSuccessListener(documentSnapshot2 -> {
                                    if (user_type.equals("PrivateClient"))
                                        user_FullName.set(documentSnapshot2.getString("full_name"));
                                    else
                                        user_FullName.set(documentSnapshot2.getString("business_name"));
                                }));

                        getDataTasks.add(db.collection("CounterForUniqueID")
                                .document("value")
                                .get()
                                .addOnSuccessListener(documentSnapshot3 -> {
                                    counter.set(documentSnapshot3.getString("counter"));
                                }));

                        Tasks.whenAllSuccess(getDataTasks).addOnSuccessListener(objects -> {

                            List<Task<Void>> tasks = new ArrayList<>();

                            Map<String, Object> data = new HashMap<>();
                            data.put("user_fullName", user_FullName.get());
                            data.put("business_name", business_name.get());
                            data.put("payment_method", "wallet");
                            data.put("from_currency", from_currency);
                            data.put("to_currency", to_currency);
                            data.put("from_amount", from_amount);
                            data.put("to_amount", to_amount);
                            data.put("date", date);
                            data.put("status", "pending");
                            data.put("id", client_user_name + "*" + business_user_name + "*" + counter.get());
                            data.put("client_type", user_type);
                            data.put("change_profit", String.valueOf(change_profit.get()));

                            tasks.add(db.collection(user_type)
                                    .document(client_user_name)
                                    .collection("OrdersByMe")
                                    .document(client_user_name + "*" + business_user_name + "*" + counter.get())
                                    .set(data));

                            tasks.add(db.collection("BusinessClient")
                                    .document(business_user_name)
                                    .collection("OrdersForMe")
                                    .document(client_user_name + "*" + business_user_name + "*" + counter.get())
                                    .set(data));

                            Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects1 -> {
                                Map<String, Object> updated_counter = new HashMap<>();
                                updated_counter.put("counter",  String.valueOf(Integer.parseInt(counter.get()) + 1));
                                db.collection("CounterForUniqueID")
                                        .document("value")
                                        .set(updated_counter)
                                        .addOnSuccessListener(unused1 -> {
                                            Toast.makeText(context, "Order Success", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, OrderConfirm.class);
                                            intent.putExtra("orderID", client_user_name + "*" + business_user_name + "*" + counter.get());
                                            intent.putExtra("user_type",user_type);
                                            intent.putExtra("user_name",client_user_name);
                                            intent.putExtra("status", "pending");
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            context.startActivity(intent);
                                        });
                            });
                        });
                    }
                    else {
                        Toast.makeText(context, "cannot complete order- balance below 0", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void LoadOrder(Context context, String orderID, String business_user_name, TextView amount_from, TextView amount_to, TextView paymethod, TextView business_name, TextView business_address, TextView business_phone, TextView pickup_date, TextView cash_case_value, TextView currency_from, TextView currency_to, TextView status){
    ArrayList<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        Task<DocumentSnapshot> t = db.collection("BusinessClient")
                .document(business_user_name)
                .collection("OrdersForMe")
                .document(orderID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    amount_from.setText(documentSnapshot.getString("from_amount"));
                    amount_to.setText(documentSnapshot.getString("to_amount"));
                    paymethod.setText(documentSnapshot.getString("payment_method"));
                    if(paymethod.getText().toString().equals("cash")){
                        cash_case_value.setText("Price may be change");
                    }
                    business_name.setText(documentSnapshot.getString("business_name"));
                    pickup_date.setText(documentSnapshot.getString("date"));
                    currency_from.setText(documentSnapshot.getString("from_currency"));
                    currency_to.setText(documentSnapshot.getString("to_currency"));
                    status.setText(documentSnapshot.getString("status"));
                });

        Task<DocumentSnapshot> t1 = db.collection("BusinessClient")
                .document(business_user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                   business_address.setText(documentSnapshot.getString("address"));
                   business_phone.setText(documentSnapshot.getString("phone"));
                });

        tasks.add(t);
        tasks.add(t1);
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {});

    }

    @Override
    public void loadCurrencyDataPairs(Context context, String user_name, ArrayList<currency> items, RecyclerView recyclerView, ProgressBar progressBar, String user_type) {
        AtomicReference<String> localCurrency = new AtomicReference<>();
        String local = "local_currency";
        String finalLocal = local;
        db.collection(user_type)
                .document(user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    localCurrency.set(documentSnapshot.getString(finalLocal));

                    Thread t = new Thread(() -> {
                        ArrayList<String> symbols = new ArrayList<>();
                        if(!"USD".equals(localCurrency.get()))
                            symbols.add("USD" +'/'+ localCurrency.get());
                        if(!"EUR".equals(localCurrency.get()))
                            symbols.add("EUR" +'/'+ localCurrency.get());
                        if(!"CNY".equals(localCurrency.get()))
                            symbols.add("CNY" +'/'+ localCurrency.get());
                        if(!"ILS".equals(localCurrency.get()))
                            symbols.add("ILS" +'/'+ localCurrency.get());
                        if(!"GBP".equals(localCurrency.get()))
                            symbols.add("GBP" +'/'+ localCurrency.get());

                        HashMap<String, ArrayList<Float>> currency_data = api.getCloseAndChangePrice(symbols);

                        ((Activity)context).runOnUiThread(() -> {
                            if (!localCurrency.get().equals("USD") && currency_data.get("USD") != null && currency_data.get("USD").size() > 0)
                                items.add(new currency(R.drawable.usd,""+currency_data.get("USD").get(0) + currenciesToSymbol.get(localCurrency.get()),""+(currency_data.get("USD").get(1)) + "%", "USD"));
                            if (!localCurrency.get().equals("EUR") && currency_data.get("EUR") != null && currency_data.get("EUR").size() > 0)
                                items.add(new currency(R.drawable.eur,""+currency_data.get("EUR").get(0) + currenciesToSymbol.get(localCurrency.get()),""+(currency_data.get("EUR").get(1)) + '%',"EUR"));
                            if (!localCurrency.get().equals("CNY") && currency_data.get("CNY") != null && currency_data.get("CNY").size() > 0)
                                items.add(new currency(R.drawable.cny,""+currency_data.get("CNY").get(0) + currenciesToSymbol.get(localCurrency.get()),""+(currency_data.get("CNY").get(1)) + '%', "CNY"));
                            if (!localCurrency.get().equals("ILS") && currency_data.get("ILS") != null && currency_data.get("ILS").size() > 0)
                                items.add(new currency(R.drawable.ils,""+currency_data.get("ILS").get(0) + currenciesToSymbol.get(localCurrency.get()),""+(currency_data.get("ILS").get(1)) + '%', "ILS"));
                            if (!localCurrency.get().equals("GBP") && currency_data.get("GBP") != null && currency_data.get("GBP").size() > 0)
                                items.add(new currency(R.drawable.gbp,""+currency_data.get("GBP").get(0) + currenciesToSymbol.get(localCurrency.get()),""+(currency_data.get("GBP").get(1)) + '%', "GBP"));

                            AdapterCurrency adapterCurrency = new AdapterCurrency(context,items);
                            recyclerView.setAdapter(adapterCurrency);
                            progressBar.setVisibility(View.INVISIBLE);
                        });

                    });
                    t.start();
                });
    }

    @Override
    public void loadOrdersAsClient(Context context, String user_name, String userType, ArrayList<Order> items, RecyclerView recyclerView) {
        db.collection(userType)
                .document(user_name)
                .collection("OrdersByMe")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> orders = queryDocumentSnapshots.getDocuments();
                    for (int i = orders.size() - 1; i >= 0; i--) {
                        Order tmp = new Order(orders.get(i).getString("id"), orders.get(i).getString("from_currency"), orders.get(i).getString("to_currency"),
                                orders.get(i).getString("from_amount"), orders.get(i).getString("to_amount"), orders.get(i).getString("date"),
                                orders.get(i).getString("business_name"), orders.get(i).getString("user_fullName"), orders.get(i).getString("status"), orders.get(i).getString("payment_method"));
                        items.add(tmp);
                    }
                        ((Activity) context).runOnUiThread(() -> {
                            AdapterOrder adapterOrder;
                            if (userType.equals("PrivateClient"))
                                adapterOrder = new AdapterOrder(context, items, "OrdersActivity");
                            else
                                adapterOrder = new AdapterOrder(context, items, "BusinessOrdersActivity");
                            recyclerView.setAdapter(adapterOrder);
                        });
                });
    }

    @Override
    public void loadOrdersAsBusiness(Context context, String user_name, String user_type, ArrayList<Order> pendding_items, ArrayList<Order> canceled_items,ArrayList<Order> approve_items,ArrayList<Order> complete_items, RecyclerView orders_RV,String listClicked) {

        db.collection(user_type)
                .document(user_name)
                .collection("OrdersForMe")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    List<DocumentSnapshot> orders = queryDocumentSnapshots.getDocuments();
                    pendding_items.clear();
                    canceled_items.clear();
                    approve_items.clear();
                    complete_items.clear();

                    for (int i = orders.size() - 1; i >= 0; i--) {
                        Order tmp = new Order(orders.get(i).getString("id"), orders.get(i).getString("from_currency"), orders.get(i).getString("to_currency"),
                                orders.get(i).getString("from_amount"), orders.get(i).getString("to_amount"), orders.get(i).getString("date"),
                                orders.get(i).getString("business_name"), orders.get(i).getString("user_fullName"), orders.get(i).getString("status"), orders.get(i).getString("payment_method"));
                        if (orders.get(i).getString("status").equals("pending"))
                            pendding_items.add(tmp);
                        if (orders.get(i).getString("status").equals("canceled"))
                            canceled_items.add(tmp);
                        if (orders.get(i).getString("status").equals("approve"))
                            approve_items.add(tmp);
                        if (orders.get(i).getString("status").equals("complete"))
                            complete_items.add(tmp);
                    }

                    ((Activity) context).runOnUiThread(() -> {
                        AdapterOrder adapterOrderPendding, adapterOrderCanceled, adapterOrderApprove, adapterOrderComplete;
                        adapterOrderPendding = new AdapterOrder(context, pendding_items, "BusinessOrdersActivity");
                        adapterOrderCanceled = new AdapterOrder(context, canceled_items, "BusinessOrdersActivity");
                        adapterOrderApprove = new AdapterOrder(context, approve_items, "BusinessOrdersActivity");
                        adapterOrderComplete = new AdapterOrder(context, complete_items, "BusinessOrdersActivity");

                        if (listClicked.equals("pending"))
                            orders_RV.setAdapter(adapterOrderPendding);
                        if (listClicked.equals("canceled"))
                            orders_RV.setAdapter(adapterOrderCanceled);
                        if (listClicked.equals("approve"))
                            orders_RV.setAdapter(adapterOrderApprove);
                        if (listClicked.equals("complete"))
                            orders_RV.setAdapter(adapterOrderComplete);
                    });
                });
    }

    @Override
    public void openOnMaps(Context context, String address) {
        Thread t = new Thread(()->{
            HashMap<String, String> res = locationDataApi.GetGeoLocation(address);
            String uri = "http://maps.google.com/maps?q=loc:" + res.get("lan") + "," + res.get("long");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            context.startActivity(intent);
        });
        t.start();
    }

    @Override
    public void loadBusinessOrder(String orderID, String user_name, TextView order_status_value, TextView amount_value, TextView currency_name_value, TextView receive_value, TextView to_currency_name_value, TextView payment_method_value, TextView client_name_value, TextView phone_value, TextView pickup_date_value, Button approve_btn, Button cancel_btn, Button scan_btn) {
        db.collection("BusinessClient")
                .document(user_name)
                .collection("OrdersForMe")
                .document(orderID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    order_status_value.setText(documentSnapshot.getString("status"));
                    amount_value.setText(documentSnapshot.getString("from_amount"));
                    currency_name_value.setText(documentSnapshot.getString("from_currency"));
                    receive_value.setText(documentSnapshot.getString("to_amount"));
                    to_currency_name_value.setText(documentSnapshot.getString("to_currency"));
                    payment_method_value.setText(documentSnapshot.getString("payment_method"));
                    client_name_value.setText(documentSnapshot.getString("user_fullName"));
                    pickup_date_value.setText(documentSnapshot.getString("date"));

                    String client_user_name = documentSnapshot.getString("id").split("\\*")[0];

                    if (!order_status_value.getText().toString().equals("pending")){
                        approve_btn.setVisibility(View.INVISIBLE);
                        cancel_btn.setVisibility(View.INVISIBLE);
                    }

                    if (!order_status_value.getText().toString().equals("approve")){
                        scan_btn.setVisibility(View.INVISIBLE);
                    }

                    if (order_status_value.getText().toString().equals("approve")){
                        scan_btn.setVisibility(View.VISIBLE);
                    }

                    db.collection(documentSnapshot.getString("client_type"))
                            .document(client_user_name)
                            .get()
                            .addOnSuccessListener(documentSnapshot1 -> {
                                phone_value.setText(documentSnapshot1.getString("phone"));
                            });

                });
    }

    private void refundUser(String client_user_name, TextView amount_value, TextView currency_name_value, Context context) {
        AtomicReference<String> user_type = new AtomicReference<>();
        Task<DocumentSnapshot> task_client = db.collection("PrivateClient")
                .document(client_user_name)
                .get();

        Task<DocumentSnapshot> task_business = db.collection("BusinessClient")
                .document(client_user_name)
                .get();

        List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
        tasks.add(task_client);
        tasks.add(task_business);

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {
            DocumentSnapshot client = (DocumentSnapshot) objects.get(0);
            if (client.exists()) {
                user_type.set("PrivateClient");
            } else {
                user_type.set("BusinessClient");
            }
            updateBalance(client_user_name, user_type.get(), currency_name_value.getText().toString() , Float.parseFloat(amount_value.getText().toString()), "+", context, null);

        });
    }

    @Override
    public void changeOrderStatus(String orderID, String user_name, String new_status, Context context, TextView order_status_value, Button approve_btn, Button cancel_btn, TextView payment_method_value, TextView amount_value,TextView currency_name_value, Button scan_btn) {
        String business_user_name = orderID.split("\\*")[1];
        String client_user_name = orderID.split("\\*")[0];
        ArrayList<Task> tasks = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("status", new_status);
        Task<Void> update_business = db.collection("BusinessClient")
                .document(business_user_name)
                .collection("OrdersForMe")
                .document(orderID)
                .update(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Order " + new_status, Toast.LENGTH_SHORT).show();
                    order_status_value.setText(new_status);

                    if (!order_status_value.equals("pending")){
                        approve_btn.setVisibility(View.INVISIBLE);
                        cancel_btn.setVisibility(View.INVISIBLE);
                        if (order_status_value.getText().toString().equals("canceled") && !payment_method_value.getText().toString().equals("cash")){
                            refundUser(client_user_name, amount_value, currency_name_value, context);
                        }
                        else{
                            updateBalance(user_name, "BusinessClient", currency_name_value.getText().toString() , Float.parseFloat(amount_value.getText().toString()), "+", context, null);
                            scan_btn.setVisibility(View.VISIBLE);
                        }
                    }
                });
        Task<Void> update_client = db.collection("PrivateClient")
                .document(client_user_name)
                .collection("OrdersByMe")
                .document(orderID)
                .update(data);

        tasks.add(update_business);
        tasks.add(update_client);
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {});
    }

    @Override
    public void updateBusinessRate(int rating, String business_user_name) {
        AtomicReference<Float> rate = new AtomicReference<>((float) 0);
        AtomicReference<Integer> num_of_rates = new AtomicReference<>(0);

        db.collection("BusinessClient")
                .document(business_user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.getString("rate") == null) {
                        rate.set((float) 0);
                        num_of_rates.set(1);
                    }
                    else {
                        rate.set(Float.parseFloat(documentSnapshot.getString("rate")));
                        num_of_rates.set(Integer.parseInt(documentSnapshot.getString("num_of_rates")) + 1);
                    }

                    rate.set(rate.get() + (rating - rate.get())/num_of_rates.get());

                    Map<String, Object> data = new HashMap<>();
                    data.put("rate", String.valueOf(rate));
                    data.put("num_of_rates", String.valueOf(num_of_rates));

                    db.collection("BusinessClient")
                            .document(business_user_name)
                            .update(data);
                });
    }

    @Override
    public void loadBusinessRate(String user_name, RatingBar ratingBar, TextView number_of_rates_value) {
        db.collection("BusinessClient")
                .document(user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.getString("rate") != null){
                        ratingBar.setRating(Float.parseFloat(documentSnapshot.getString("rate")));
                        number_of_rates_value.setText(documentSnapshot.getString("num_of_rates"));
                    }
                    else {
                        ratingBar.setRating(0);
                        number_of_rates_value.setText("0");

                    }
                });
    }

    @Override
    public void CompleteOrder(Context context, String order_id) {
        String business_user_name = order_id.split("\\*")[1];
        String client_user_name = order_id.split("\\*")[0];
        HashMap<String, Object> status = new HashMap<>();
        status.put("status", "complete");

        ArrayList<Task> tasks = new ArrayList<>();

        Task<DocumentSnapshot> update_business_statistics = db.collection("BusinessClient")
                .document(business_user_name)
                .collection("OrdersForMe")
                .document(order_id)
                .get().addOnSuccessListener(documentSnapshot -> {
                    Float profit = Float.parseFloat(documentSnapshot.getString("change_profit"));
                    db.collection("BusinessClient")
                            .document(business_user_name)
                            .get().addOnSuccessListener(documentSnapshot1 -> {
                                Float new_total = Float.parseFloat(documentSnapshot1.getString("total_profit")) + profit;
                                int new_number_of_trades = Integer.parseInt(documentSnapshot1.getString("number_of_trades")) + 1;
                                Float new_avg = new_total / new_number_of_trades;
                                HashMap<String, Object> data_to_update = new HashMap<>();
                                data_to_update.put("total_profit", String.valueOf(new_total));
                                data_to_update.put("number_of_trades", String.valueOf(new_number_of_trades));
                                data_to_update.put("avg_profit", String.valueOf(new_avg));
                                db.collection("BusinessClient")
                                        .document(business_user_name)
                                        .update(data_to_update);
                    });
                });



        Task<Void> update_business_task = db.collection("BusinessClient")
                .document(business_user_name)
                .collection("OrdersForMe")
                .document(order_id)
                .update(status);

        Task<Void> update_client_task = db.collection("PrivateClient")
                .document(client_user_name)
                .collection("OrdersByMe")
                .document(order_id)
                .update(status);

        tasks.add(update_business_task);
        tasks.add(update_client_task);
        tasks.add(update_business_statistics);
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {}).addOnFailureListener(e -> Toast.makeText(context, "Failed to update order status", Toast.LENGTH_LONG).show());
    }

    @Override
    public void SortByPrice(ArrayList<Search> filter_list, String from_currency, String to_currency, RecyclerView recyclerView) {
        if(filter_list.size() > 1 && !from_currency.equals(to_currency)){
            List<Task> tasks = new ArrayList<>();
            HashMap<String, Float> change_to_comission = new HashMap<>();
            for (Search s: filter_list) {
                Task tmp = db.collection("BusinessClient")
                        .document(s.userName)
                        .collection("currencyComission")
                        .document(from_currency + '*' + to_currency)
                        .get();
                tasks.add(tmp);
            }
            Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {
                for (int i = 0; i < objects.size(); i++){
                    DocumentSnapshot tmp = (DocumentSnapshot) objects.get(i);
                    change_to_comission.put(filter_list.get(i).userName, Float.valueOf(tmp.getString(from_currency + to_currency)));
                }
                Collections.sort(filter_list, (o1, o2) -> {
                    if (change_to_comission.get(o1.userName) < change_to_comission.get(o2.userName)){
                        return -1;
                    }
                    else {return 1; }
                });
                recyclerView.getAdapter().notifyDataSetChanged();
            });
        }
    }

    @Override
    public void loadBusinessStatistics(String user_name, TextView numOrderValue, TextView totalProfitValue, TextView averagePerTrade) {
        db.collection("BusinessClient")
                .document(user_name)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    numOrderValue.setText(df.format(Float.parseFloat(documentSnapshot.getString("number_of_trades"))));
                    totalProfitValue.setText(df.format(Float.parseFloat(documentSnapshot.getString("total_profit"))));
                    averagePerTrade.setText(df.format(Float.parseFloat(documentSnapshot.getString("avg_profit"))));
                });
    }

    @Override
    public void updateOrderRateStatus(String orderID) {
        String business_user_name = orderID.split("\\*")[1];
        String client_user_name = orderID.split("\\*")[0];
        ArrayList<Task> tasks = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("rated", "true");
        Task<Void> update_business = db.collection("BusinessClient")
                .document(business_user_name)
                .collection("OrdersForMe")
                .document(orderID)
                .update(data);

        Task<Void> update_client = db.collection("PrivateClient")
                .document(client_user_name)
                .collection("OrdersByMe")
                .document(orderID)
                .update(data);

        tasks.add(update_business);
        tasks.add(update_client);
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(objects -> {});
    }

    @Override
    public void checkOrderRated(String orderID, Context context, RateFlag rateFlag) {
        String business_user_name = orderID.split("\\*")[1];
        db.collection("BusinessClient")
                .document(business_user_name)
                .collection("OrdersForMe")
                .document(orderID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.getString("status").equals("complete") && documentSnapshot.getString("rated") == null) {
                       rateFlag.setRate(1);
                    }
                });
    }

    public void LoadOrdersStatus(Context context, TextView orders_for_today, TextView new_orders, TextView cash_orders, String user_type, String user_name) {
        db.collection(user_type)
                .document(user_name)
                .collection("OrdersForMe")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> orders = queryDocumentSnapshots.getDocuments();
                   int order_for_today_cnt = 0 , new_orders_cnt = 0, cash_orders_cnt = 0;
                   SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                   String current_date = formatter.format(new Date());

                    for (DocumentSnapshot doc: orders) {
                        if (doc.getString("date").equals(current_date)){
                            order_for_today_cnt ++;
                        }
                        if(doc.getString("status").equals("pending")){
                            new_orders_cnt ++;
                        }
                        if(doc.getString("payment_method").equals("cash")){
                            cash_orders_cnt ++;
                        }
                    }
                    int finalOrder_for_today_cnt = order_for_today_cnt;
                    int finalNew_orders_cnt = new_orders_cnt;
                    int finalCash_orders_cnt = cash_orders_cnt;
                    ((Activity)context).runOnUiThread(()->{
                        orders_for_today.setText(""+finalOrder_for_today_cnt);
                        new_orders.setText(""+ finalNew_orders_cnt);
                        cash_orders.setText(""+ finalCash_orders_cnt);
                    });
                });
    }


    public void saveOpenHours(Context context,String user_name, ArrayList<OpenHours> openHours, Intent intent) {

        List<Task<Void>> tasks = new ArrayList<>();


        for (int i = 0; i < openHours.size(); i++) {
            Map<String, Object> data = new HashMap<>();
            data.put("open", openHours.get(i).getOpen());
            data.put("close", openHours.get(i).getClose());

            tasks.add(db.collection("BusinessClient")
                    .document(user_name)
                    .collection("OpenHours")
                    .document(indexToDay.get(i))
                    .set(data));
        };

        Tasks
                .whenAllSuccess(tasks).addOnSuccessListener(objects -> {
            Toast.makeText(context, "business updated successfully", Toast.LENGTH_SHORT).show();
        })
                .addOnFailureListener(e -> Toast.makeText(context, "Fail update business : " + e.toString(), Toast.LENGTH_LONG).show());
    }

    public void checkExistPhoto(TextView value, String user_name, String type) {
        String endOfPath = "";
        switch (type) {
            case "passport_photo":
                endOfPath = KEY_PASSPORT_PHOTO;
                break;
            case "business_approval":
                endOfPath = KEY_BUSINESS_APPROVAL;
                break;
            case "owner_id":
                endOfPath = KEY_OWNER_ID;
                break;

        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String path = "images/" + user_name + "/" + endOfPath + ".jpg";
        StorageReference pathReference = storageRef.child(path);
        Task<byte[]> photo = pathReference.getBytes(2000000000);
        photo.addOnSuccessListener(bytes -> {
            value.setText("Exist");
        })
        .addOnFailureListener(e -> {
            value.setText("Non Exist");
        });
    }

    public void checkExistPassport(Context context, String business_user_name, String client_user_name, String user_type) {

        if (user_type.equals("PrivateClient")) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            String path = "images/" + client_user_name + "/" + KEY_PASSPORT_PHOTO + ".jpg";
            StorageReference pathReference = storageRef.child(path);
            Task<byte[]> photo = pathReference.getBytes(2000000000);
            photo.addOnSuccessListener(bytes -> {
                Intent intent = new Intent(context, OrderPage.class);
                intent.putExtra("business_user_name", business_user_name);
                intent.putExtra("client_user_name", client_user_name);
                intent.putExtra("user_type", user_type);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                context.startActivity(intent);
            })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "must upload passport photo first", Toast.LENGTH_SHORT).show();
                    });
        }
        else {
            Intent intent = new Intent(context, OrderPage.class);
            intent.putExtra("business_user_name", business_user_name);
            intent.putExtra(client_user_name, client_user_name);
            intent.putExtra("user_type", user_type);
            context.startActivity(intent);
        }
    }

}

