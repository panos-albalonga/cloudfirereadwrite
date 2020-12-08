package com.albalonga.cloudfirereadwrite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Table extends AppCompatActivity {
    private int mySeat=-99;
    private String myName;
    private String tblCode;
    private String myUsrId;
    private boolean registered = false;
    private ArrayList<Player> playersList = new ArrayList<Player>();
//    private int cntPlayers;
    private ImageView imgPlrMe, sqTable;
    private TextView txvPlr;
    private ConstraintLayout tableLayout;
    private ConstraintSet cnstrSet;
//    private TextView txvTblCode;
    private TextView txvPlayer;

//    private FirebaseFirestore fbDb;
    private CollectionReference playerCollRef;
    private CollectionReference colDeck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        tblCode = intent.getStringExtra("TableCode");
        myUsrId = intent.getStringExtra("UserId");
        myName  = intent.getStringExtra("Player");

        tableLayout = new ConstraintLayout(this);
        cnstrSet = new ConstraintSet();

        sqTable = new ImageView(this);
        tableLayout.addView(sqTable);
        sqTable.setId(99);
        sqTable.setImageResource(R.drawable.table);

        cnstrSet.constrainHeight(sqTable.getId(), ConstraintSet.WRAP_CONTENT);
        cnstrSet.constrainWidth(sqTable.getId(), ConstraintSet.WRAP_CONTENT);
        cnstrSet.connect(sqTable.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        cnstrSet.connect(sqTable.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        cnstrSet.connect(sqTable.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        cnstrSet.connect(sqTable.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

        txvPlayer = new TextView(this);
        tableLayout.addView(txvPlayer);
        txvPlayer.setId(91);
        txvPlayer.setText("Tbl code: " + tblCode);
        cnstrSet.constrainHeight(txvPlayer.getId(), ConstraintSet.WRAP_CONTENT);
        cnstrSet.constrainWidth(txvPlayer.getId(), ConstraintSet.WRAP_CONTENT);
        cnstrSet.connect(txvPlayer.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 16);
        cnstrSet.connect(txvPlayer.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 16);

//        final String playerCollection = tblCode + "_Players";
        final String playerCollection = tblCode;
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        playerCollRef = db.collection(playerCollection);

        //************ Setup Player Initial Listener
        playerCollRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("FROM", "OnComplete - Registered:" + registered + "mySeat: " + mySeat + " " + myName);

                    for (QueryDocumentSnapshot dbPlayer : task.getResult()) {
                        String dbPlrUsr = dbPlayer.getId().toString();
                        if(dbPlrUsr == myUsrId){
                            registered = true;
                        }
                        Log.d("DbPlayers"," plrID:" + dbPlayer.getId().toString() + " myID:" + myUsrId);
                        String dbPlrNickName = dbPlayer.get("nickName").toString();
                        int dblPlrSeat = Integer.parseInt(dbPlayer.get("seat").toString()); // 1-2. Assign to String first - Then parse it into Integer
//                        newPlr = new Player(dbPlrNickName, dblPlrSeat);
//                        playersList.add(newPlr);
                        Log.d("DbPlayers"," OLD=> " + dbPlayer.getData() + dbPlrNickName +" " + dblPlrSeat);
                    }
                    if(!registered){
                        // If user NOT already registered
                        if(mySeat < 0){
                            mySeat = task.getResult().size();
                            Log.d("FROM", "onComplete if(mySeat<0)==> mySeat= " + mySeat + " " + myName);
/*
                        Player newPlr = new Player(plrNickName, cntPlayers); // Create new player for ME.
                        playersList.add(newPlr);
                        for (QueryDocumentSnapshot dbPlayer : task.getResult()) {
                            String dbPlrNickName = dbPlayer.get("nickName").toString();
                            int dblPlrSeat = Integer.parseInt(dbPlayer.get("seat").toString()); // 1-2. Assign to String first - Then parse it into Integer
                            newPlr = new Player(dbPlrNickName, dblPlrSeat);
                            playersList.add(newPlr);
                            Log.d("DbPlayers"," OLD=> " + dbPlayer.getData() + dbPlrNickName +" " + dblPlrSeat);
                        }
*/
                            Map<String, Object> player = new HashMap<>();
                            player.put("nickName", myName);
                            player.put("seat", mySeat);
                            db.collection(playerCollection).document(myUsrId).set(player);
                        }
                    }
                    //drawPlayers();
                } else {
                    Log.d("FROM", "AddOnComplete Task NOT successful Seat>=0 => " + mySeat + " " + myName);
                    //Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });

        //************ Setup Player Update Listener
        playerCollRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("PANOS-Table-queryPlrs", "Snapshop listen:error", e);
                    return;
                }
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    String plrUsrId = dc.getDocument().getId();
                    if(plrUsrId == myUsrId){
                        registered = true;
                    }
/*
                    if(mySeat >= 0){
                        switch (dc.getType()) {
                            case ADDED:
                                Log.d("FROM", "AddSnap mySeat: " + mySeat + " get Doc:" + dc.getDocument().getData().values().toString());

                                String newPlayer = dc.getDocument().get("nickName").toString();
                                int newSeat = Integer.parseInt(dc.getDocument().get("seat").toString());
                                crtPlrView(newPlayer, newSeat);
                                if(newSeat == mySeat){
                                    Log.d("FROM", "AddSnap== mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
                                    drawPlrView(imgPlrMe, txvPlr, "B");
                                }else if(newSeat == mySeat-1){
                                    Log.d("FROM", "AddSnap-1 mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
                                    drawPlrView(imgPlrMe, txvPlr, "L");
                                }else if(newSeat == mySeat-2){
                                    Log.d("FROM", "AddSnap-2 mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
                                    drawPlrView(imgPlrMe, txvPlr, "T");
                                }else if(newSeat == mySeat-3){
                                    Log.d("FROM", "AddSnap-3 mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
                                    drawPlrView(imgPlrMe, txvPlr, "R");
                                }else if(newSeat == mySeat+1){
                                    Log.d("FROM", "AddSnap+1 mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
                                    drawPlrView(imgPlrMe, txvPlr, "R");
                                }else if(newSeat == mySeat+2){
                                    Log.d("FROM", "AddSnap+2 mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
                                    drawPlrView(imgPlrMe, txvPlr, "T");
                                }else if(newSeat == mySeat+3){
                                    Log.d("FROM", "AddSnap+3 mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
                                    drawPlrView(imgPlrMe, txvPlr, "L");
                                };
//                            txvPlayer.setText("Add: " + newPlayer + " seat " + newSeat);
//                            String newPlrNickName = newPlayer;
//                            int newlPlrSeat = newSeat;
//                            Player newPlr = new Player(newPlrNickName, newlPlrSeat);


//                            playersList.add(newPlr);

                                //drawNewPlayer();
                                break;
                            case MODIFIED:
//                            txvPlayer.setText("Mod:"+dc.getDocument().getData().values().toString());
                                break;
                            case REMOVED:
//                            txvPlayer.setText("Rem:"+dc.getDocument().getData().values().toString());
                                break;
                        }
                    }
*/
                    Log.d("FROM", "AddSnap Reg:" + registered + " mySeat: " + mySeat + " " + dc.getType() + " " + dc.getDocument().getData().values().toString() + " Usr:" + plrUsrId);
                    switch (dc.getType()) {
                        case ADDED:
                            String dbPlrName = dc.getDocument().get("nickName").toString();
                            int dbPlrSeat = Integer.parseInt(dc.getDocument().get("seat").toString());
                            Player newPlr = new Player(dbPlrName, dbPlrSeat);
                            playersList.add(newPlr);

                            if(registered){    // if(mySeat!=-99){
                                if(dbPlrSeat == mySeat){
                                    for(Player plr: playersList){
                                        Log.d("FROM", "AddSnap== mySeat:" + mySeat + " dbSeat:" + dbPlrSeat + "-" + dbPlrName + " Data" + dc.getDocument().getData().values().toString());
                                        crtPlrView(plr.getName(), plr.getSeat());
                                        posPlrView(plr.getSeat());
                                    }
                                }else{
                                    crtPlrView(dbPlrName, dbPlrSeat);
                                    posPlrView(dbPlrSeat);
/*

                                    if(dbPlrSeat == mySeat-1){
                                        Log.d("FROM", "AddSnap-1 mySeat:" + mySeat + " dbPlrSeat:" + dbPlrSeat + "-" + dbPlrName + " Data" + dc.getDocument().getData().values().toString());
                                        drawPlrView(imgPlrMe, txvPlr, "L");
                                    }else if(dbPlrSeat == mySeat-2){
                                        Log.d("FROM", "AddSnap-2 mySeat:" + mySeat + " dbPlrSeat:" + dbPlrSeat + "-" + dbPlrName + " Data" + dc.getDocument().getData().values().toString());
                                        drawPlrView(imgPlrMe, txvPlr, "T");
                                    }else if(dbPlrSeat == mySeat-3){
                                        Log.d("FROM", "AddSnap-3 mySeat:" + mySeat + " dbPlrSeat:" + dbPlrSeat + "-" + dbPlrName + " Data" + dc.getDocument().getData().values().toString());
                                        drawPlrView(imgPlrMe, txvPlr, "R");
                                    }else if(dbPlrSeat == mySeat+1){
                                        Log.d("FROM", "AddSnap+1 mySeat:" + mySeat + " dbPlrSeat:" + dbPlrSeat + "-" + dbPlrName + " Data" + dc.getDocument().getData().values().toString());
                                        drawPlrView(imgPlrMe, txvPlr, "R");
                                    }else if(dbPlrSeat == mySeat+2){
                                        Log.d("FROM", "AddSnap+2 mySeat:" + mySeat + " dbPlrSeat:" + dbPlrSeat + "-" + dbPlrName + " Data" + dc.getDocument().getData().values().toString());
                                        drawPlrView(imgPlrMe, txvPlr, "T");
                                    }else if(dbPlrSeat == mySeat+3){
                                        Log.d("FROM", "AddSnap+3 mySeat:" + mySeat + " dbPlrSeat:" + dbPlrSeat + "-" + dbPlrName + " Data" + dc.getDocument().getData().values().toString());
                                        drawPlrView(imgPlrMe, txvPlr, "L");
                                    };

*/
                                }
                            }
                            break;
                        case MODIFIED:
//                            txvPlayer.setText("Mod:"+dc.getDocument().getData().values().toString());
                            break;
                        case REMOVED:
//                            txvPlayer.setText("Rem:"+dc.getDocument().getData().values().toString());
                            break;
                    }
                }
            }
        });

        String deckCol = tblCode + "_Deck";
        Deck myDeck = new Deck();
//        myDeck.upload(deckCol);
//        Log.d("DECKIN", "panos before>>"+deckCol+"<<panos");

        cnstrSet.applyTo(tableLayout);
        setContentView(tableLayout);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
/*
    private void drawPlayers(){

        String lstPlrNickName;
        int lstPlrSeat;
        int cntArr=0;
        int lstSize = playersList.size();
        View prvView = null;
        Log.d("CNSTR", "List size=" + playersList.size());

        for(Player plr: playersList){
            cntArr++;
            lstPlrNickName = plr.getNickName();
            lstPlrSeat = plr.getSeat();
            int imgId = lstPlrSeat + 1;
            int txtId = lstPlrSeat + 51;

            imgPlrMe = new ImageView(this);
            imgPlrMe.setId(imgId);
            imgPlrMe.setImageResource(R.drawable.player01sm);
            txvPlr = new TextView(this);
            txvPlr.setId(txtId);
            txvPlr.setText(lstPlrSeat+"-"+lstPlrNickName);
            tableLayout.addView(imgPlrMe);
            tableLayout.addView(txvPlr);
            boolean lastV = playersList.indexOf(plr) == playersList.size() - 1; // Check if last View
            if(prvView == null){
                txvPlr.setText("Me:"+lstPlrSeat+"-"+lstPlrNickName);
                drawPlrView(imgPlrMe, txvPlr, "B");
                Log.d("CNSTR-1", imgPlrMe.getId() + " Seat:" +lstPlrSeat + " Name " + lstPlrNickName);
            }else{
                if(lstPlrSeat == lstSize-2){
                    drawPlrView(imgPlrMe, txvPlr, "L");
                    Log.d("CNSTR-2", imgPlrMe.getId() + " Seat:" + lstPlrSeat + " Name " + lstPlrNickName);
                }else if(lstPlrSeat == lstSize-3){
                    drawPlrView(imgPlrMe, txvPlr, "T");
                    Log.d("CNSTR-3", imgPlrMe.getId() + " Seat:" + lstPlrSeat + " Name " + lstPlrNickName);
                }else if(lstPlrSeat == lstSize-4){
                    drawPlrView(imgPlrMe, txvPlr, "R");
                    Log.d("CNSTR-4", imgPlrMe.getId() + " Seat:" + lstPlrSeat + " Name " + lstPlrNickName);
                }
            }
            prvView = imgPlrMe;
            cnstrSet.applyTo(tableLayout);
            prvView = imgPlrMe;


            Log.d("DRAW-PLAYERS", +cntArr + " Nick: " + lstPlrSeat + " Seat:" + lstPlrNickName);
        }
    }
*/
    private void crtPlrView(String aName, int aSeat){
        String  plrName = aName;
        int     plrSeat = aSeat;
        Log.d("FROM", "crtPlrView:" + plrSeat + "-" + plrName);

        int imgId = plrSeat + 1;
        int txtId = plrSeat + 51;

        imgPlrMe = new ImageView(this);
        imgPlrMe.setId(imgId);
        imgPlrMe.setImageResource(R.drawable.player01sm);

        txvPlr = new TextView(this);
        txvPlr.setId(txtId);
        txvPlr.setText(plrSeat+"-"+plrName);

        tableLayout.addView(imgPlrMe);
        tableLayout.addView(txvPlr);

    }

    private void crtPlrViews(Player plr){
        String  plrName = plr.getName();
        int     plrSeat = plr.getSeat();
        Log.d("FROM", "crtPlrView:" + plrSeat + "-" + plrName);

        int imgId = plrSeat + 1;
        int txvId = plrSeat + 51;

        imgPlrMe = new ImageView(this);
        imgPlrMe.setId(imgId);
        imgPlrMe.setImageResource(R.drawable.player01sm);

        txvPlr = new TextView(this);
        txvPlr.setId(txvId);
        txvPlr.setText(plrSeat+"-"+plrName);

        tableLayout.addView(imgPlrMe);
        tableLayout.addView(txvPlr);
    }

    private void drawPlrView(ImageView imgV, TextView txtV, String pos){
        Log.d("FROM", "POS=IN:" + pos);     // Bottom
        switch (pos){
            case "B":
                Log.d("FROM", "POS=B:" + pos);     // Bottom
                cnstrSet.constrainHeight(imgV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.constrainWidth(imgV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.connect(imgV.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 8);
                cnstrSet.connect(imgV.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 8);
                cnstrSet.connect(imgV.getId(), ConstraintSet.TOP, sqTable.getId(), ConstraintSet.BOTTOM, 8);
                cnstrSet.connect(imgV.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 8);
                cnstrSet.constrainHeight(txtV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.constrainWidth(txtV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.connect(txtV.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 8);
                cnstrSet.connect(txtV.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 8);
                cnstrSet.connect(txtV.getId(), ConstraintSet.TOP, imgV.getId(), ConstraintSet.BOTTOM, 8);
                cnstrSet.connect(txtV.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 8);
                cnstrSet.applyTo(tableLayout);
                break;
            case "L":
                Log.d("FROM", "POS=L:" + pos);     // Left
                cnstrSet.constrainHeight(imgV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.constrainWidth(imgV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.connect(imgV.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 8);
                cnstrSet.connect(imgV.getId(), ConstraintSet.RIGHT, sqTable.getId(), ConstraintSet.LEFT, 8);
                cnstrSet.connect(imgV.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 8);
                cnstrSet.connect(imgV.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 8);
                cnstrSet.constrainHeight(txtV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.constrainWidth(txtV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.connect(txtV.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 8);
                cnstrSet.connect(txtV.getId(), ConstraintSet.RIGHT, sqTable.getId(), ConstraintSet.LEFT, 8);
                cnstrSet.connect(txtV.getId(), ConstraintSet.TOP, imgV.getId(), ConstraintSet.BOTTOM, 8);
                cnstrSet.connect(txtV.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 8);
                cnstrSet.setVerticalBias(txtV.getId(), 0.05f);
                cnstrSet.applyTo(tableLayout);
                break;
            case "T":
                Log.d("FROM", "POS=T:" + pos);     // Top
                cnstrSet.constrainHeight(imgV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.constrainWidth(imgV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.connect(imgV.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 8);
                cnstrSet.connect(imgV.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 8);
                cnstrSet.connect(imgV.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 8);
                cnstrSet.connect(imgV.getId(), ConstraintSet.BOTTOM, sqTable.getId(), ConstraintSet.TOP, 8);
                cnstrSet.constrainHeight(txvPlr.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.constrainWidth(txvPlr.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.connect(txtV.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 8);
                cnstrSet.connect(txtV.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 8);
                cnstrSet.connect(txtV.getId(), ConstraintSet.TOP, imgV.getId(), ConstraintSet.BOTTOM, 8);
                cnstrSet.connect(txtV.getId(), ConstraintSet.BOTTOM, sqTable.getId(), ConstraintSet.TOP, 8);
                cnstrSet.setVerticalBias(txtV.getId(), 0.05f);
                cnstrSet.applyTo(tableLayout);
                break;
            case "R":
                Log.d("FROM", "POS=R:" + pos);     // Right
                cnstrSet.constrainHeight(imgV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.constrainWidth(imgV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.connect(imgV.getId(), ConstraintSet.LEFT, sqTable.getId(), ConstraintSet.RIGHT, 8);
                cnstrSet.connect(imgV.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 8);
                cnstrSet.connect(imgV.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 8);
                cnstrSet.connect(imgV.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 8);
                cnstrSet.constrainHeight(txtV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.constrainWidth(txtV.getId(), ConstraintSet.WRAP_CONTENT);
                cnstrSet.connect(txtV.getId(), ConstraintSet.LEFT, sqTable.getId(), ConstraintSet.RIGHT, 8);
                cnstrSet.connect(txtV.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 8);
                cnstrSet.connect(txtV.getId(), ConstraintSet.TOP, imgV.getId(), ConstraintSet.BOTTOM, 8);
                cnstrSet.connect(txtV.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 8);
                cnstrSet.setVerticalBias(txtV.getId(), 0.05f);
                cnstrSet.applyTo(tableLayout);
                break;
        }

    }

    private void posPlrView(int newSeat){
        if(newSeat == mySeat){
//            Log.d("FROM", "AddSnap== mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
            drawPlrView(imgPlrMe, txvPlr, "B");
        }else if(newSeat == mySeat-1){
//            Log.d("FROM", "AddSnap-1 mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
            drawPlrView(imgPlrMe, txvPlr, "L");
        }else if(newSeat == mySeat-2){
//            Log.d("FROM", "AddSnap-2 mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
            drawPlrView(imgPlrMe, txvPlr, "T");
        }else if(newSeat == mySeat-3){
//            Log.d("FROM", "AddSnap-3 mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
            drawPlrView(imgPlrMe, txvPlr, "R");
        }else if(newSeat == mySeat+1){
//            Log.d("FROM", "AddSnap+1 mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
            drawPlrView(imgPlrMe, txvPlr, "R");
        }else if(newSeat == mySeat+2){
//            Log.d("FROM", "AddSnap+2 mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
            drawPlrView(imgPlrMe, txvPlr, "T");
        }else if(newSeat == mySeat+3){
//            Log.d("FROM", "AddSnap+3 mySeat:" + mySeat + " newSeat:" + newSeat + "-" + newPlayer + " Data" + dc.getDocument().getData().values().toString());
            drawPlrView(imgPlrMe, txvPlr, "L");
        };

    }
}
