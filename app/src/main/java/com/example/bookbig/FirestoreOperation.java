package com.example.bookbig;

import android.net.Uri;
import android.util.Log;

import com.example.bookbig.bookcover.Bookcover;
import com.example.bookbig.hifive.HiFive;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FirestoreOperation {
    private static final String TAG = "FirestoreOperation: ";
    private static final String DEFAULT = "https://firebasestorage.googleapis.com/v0/b/bookbig-3ce6d.appspot.com/o/bookcover%2Fagenda.png?alt=media&token=61df2a07-fea7-4e9e-890c-03750dd60937";
    private static final String maleProfile = "https://firebasestorage.googleapis.com/v0/b/bookbig-3ce6d.appspot.com/o/bookcover%2Fuser%20boy3.png?alt=media&token=2275dd43-9ff5-4651-bbcc-8442bb864f86";
    private static final String femaleProfile = "https://firebasestorage.googleapis.com/v0/b/bookbig-3ce6d.appspot.com/o/bookcover%2Fuser%20girl3.png?alt=media&token=b38fa169-036e-41ca-866d-6d2e55c58afb";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUserId;
    private DocumentReference currentUserAccountRef;
    private CollectionReference userAccountRef;
    private CollectionReference bookcoverRef;
    private CollectionReference bookcoverTypeRef;
    private CollectionReference hifiveRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private CollectionReference likeRef;
    private CollectionReference messageRef;
    private FirebaseFirestoreSettings settings;

    public DocumentReference getCurrentUserAccountRef() {
        return currentUserAccountRef;
    }

    public CollectionReference getBookcoverTypeRef() {
        return bookcoverTypeRef;
    }

    public CollectionReference getLikeRef() {
        return likeRef;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public CollectionReference getBookcoverRef() {
        return bookcoverRef;
    }

    public FirestoreOperation() {
//        For using online data only!!!!
//        settings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(false)
//                .build();
//        db.setFirestoreSettings(settings);
        currentUserId = mAuth.getUid();
        userAccountRef = db.collection("user_account");
        currentUserAccountRef = db.collection("user_account").document(currentUserId);
        bookcoverRef = db.collection("bookcover");
        bookcoverTypeRef = db.collection("bookcover_type");
        hifiveRef = db.collection("hifive");
        likeRef = db.collection("like");
        messageRef = db.collection("message");
    }

    public void setUserAccount(Profile profile) {
        currentUserAccountRef.set(profile).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Update Successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to Update");
            }
        });
    }

    public void updateUserAccount(String name, String age, String gender) {
        String profilePicture;
        if(gender.equals("Male")){
            profilePicture = maleProfile;
        }else {
            profilePicture = femaleProfile;
        }
        Map<String, Object> userAccount = new HashMap<>();
        userAccount.put("name", name);
        userAccount.put("age", age);
        userAccount.put("gender", gender);
        userAccount.put("profilePicture", profilePicture);
        currentUserAccountRef
                .update(userAccount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Update Successful");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to Update");
            }
        });
    }


    public void updateUserMaxDistance(int distance) {
        currentUserAccountRef
                .update("maxDistance",distance)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Update Successful");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to Update");
            }
        });
    }



    public void setBookcover(final String name, final String description, Uri imageUri, final String bookcoverType) {
        if (imageUri == null) {
            String id = bookcoverRef.document().getId();
            Bookcover bookcover = new Bookcover(name, description, DEFAULT, currentUserId, id,bookcoverType);
            // Add a new data with document from email //TODO Change to phonenumber later
            bookcoverRef.document(id)
                    .set(bookcover)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Successfully Added");
                            Log.d(TAG, db.collection("bookcover").document().getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });

        } else {
            StorageReference storageRef = storage.getReference();
            final StorageReference imageRef = storageRef.child("bookcover/" + imageUri.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri photoUrl) {
                            String id = bookcoverRef.document().getId();
                            Bookcover bookcover = new Bookcover(name, description, photoUrl.toString(), currentUserId, id,bookcoverType);
                            // Add a new data with document from email //TODO Change to phonenumber later
                            bookcoverRef.document(id)
                                    .set(bookcover)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Successfully Added");
                                            Log.d(TAG, db.collection("bookcover").document().getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        }
                    });
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });
        }
    }

    public void updateBookcover(final String name, final String description, final Uri imageUri,final String bookcoverType,final String bookcoverId) {
        if (imageUri == null) {
            Map<String, Object> bookcover = new HashMap<>();
            bookcover.put("name", name);
            bookcover.put("description", description);
            bookcover.put("bookcoverType", bookcoverType);
            bookcoverRef
                    .document(bookcoverId)
                    .update(bookcover)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Update Successful");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Failed to Update");
                }
            });
        }else {
            StorageReference storageRef = storage.getReference();
            final StorageReference imageRef = storageRef.child("bookcover/" + imageUri.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(imageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri photoUrl) {
                            // Add a new data with document from email //TODO Change to phonenumber later
                            Map<String, Object> bookcover = new HashMap<>();
                            bookcover.put("name", name);
                            bookcover.put("description", description);
                            bookcover.put("photoUrl", photoUrl.toString());
                            bookcover.put("bookcoverType", bookcoverType);
                            bookcoverRef
                                    .document(bookcoverId)
                                    .update(bookcover)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Update Successful");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Failed to Update");
                                }
                            });
                        }
                    });
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });
        }
    }


//    private void getUserBookCover(final BookcoverCallback bookcoverCallback) {
//        final List<Bookcover> bookcoverList = new ArrayList<>();
//        bookcoverRef
//                .whereEqualTo("userId", currentUserId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                String name = document.getData().get("name").toString();
//                                String description = document.getData().get("description").toString();
//                                String userId = document.getData().get("userId").toString();
//                                String bookcoverId = document.getData().get("bookcoverId").toString();
//                                String photoUrl = document.getData().get("photoUrl").toString();
//                                Bookcover bookcover = new Bookcover(name, description, photoUrl, userId, bookcoverId);
//                                Log.d(TAG, bookcover.toString());
//                                bookcoverList.add(bookcover);
//                            }
//                            bookcoverCallback.onCallback(bookcoverList);
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }

    public void setLike(Bookcover bookcover) {
        String likeId = likeRef.document().getId();
        Like like = new Like(likeId, bookcover.getBookcoverId(), bookcover.getUserId(), currentUserId);
        likeRef.document(likeId)
                .set(like).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Successful to Set Like ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failed to Set Like ");
            }
        });
    }


    public void setNope(Bookcover bookcover) {
        bookcoverRef.document(bookcover.getBookcoverId())
                .update("nope", FieldValue.arrayUnion(currentUserId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Nope Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    public void isHiFive(final String bookcoverUserId) {
        likeRef
                .whereEqualTo("ownerId", currentUserId).whereEqualTo("userLikeId", bookcoverUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Log.d(TAG, "Hi Five!!!!!");
                                    String userId1 = document.getData().get("ownerId").toString();
                                    String userId2 = document.getData().get("userLikeId").toString();
                                    List<String> userIdList = new ArrayList<>();
                                    userIdList.add(userId1);
                                    userIdList.add(userId2);
                                    setHiFive(userIdList);
                                    break;
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setHiFive(final List<String> userIdList) {
        final String hifiveId = hifiveRef.document().getId();
        HiFive hiFive = new HiFive(userIdList, hifiveId);
        hifiveRef.document(hifiveId).set(hiFive).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Successful to Set Hi Five");
                updateHiFiveIdInLike(hifiveId, userIdList.get(0), userIdList.get(1));
                updateHiFiveIdInLike(hifiveId, userIdList.get(1), userIdList.get(0));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Fail to Set Hi Five");
            }
        });
    }

    private void updateHiFiveIdInLike(final String hiFiveId, String userId1, String userId2) {
        likeRef
                .whereEqualTo("ownerId", userId1).whereEqualTo("userLikeId", userId2)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        List<String> likeIdList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            if (document.exists()) {
                                String likeId = document.getData().get("likeId").toString();
                                setHiFiveId(hiFiveId, likeId);
                            }
                        }
                    }
                });
    }

    private void setHiFiveId(String hiFiveId, String likeId) {
        likeRef
                .document(likeId)
                .update("hifiveId", hiFiveId)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Like successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public CollectionReference getUserAccountRef() {
        return userAccountRef;
    }

    public CollectionReference getHifiveRef() {
        return hifiveRef;
    }

    public void logOut() {
        mAuth.signOut();
    }


    public FirebaseFirestore getDb() {
        return db;
    }

    public CollectionReference getMessageRef() {
        return messageRef;
    }

    public void updateUserLocation(String latitude, String longtitude) {
        Map<String, Object> location = new HashMap<>();
        location.put("latitude", latitude);
        location.put("longtitude", longtitude);
        currentUserAccountRef
                .update(location)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
}




