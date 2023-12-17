package com.example.accommodiq.ui.newAccommodation;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.accommodiq.dtos.AccommodationCreateDto;
import com.example.accommodiq.dtos.AccommodationDetailsDto;
import com.example.accommodiq.models.Availability;
import com.example.accommodiq.services.interfaces.AccommodationApiService;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewAccommodationViewModel extends ViewModel {
    private final MutableLiveData<List<Availability>> availabilityListLiveData = new MutableLiveData<>();
    private List<Availability> availabilityList = new ArrayList<>();
    private final AccommodationApiService apiService;
    public NewAccommodationViewModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(AccommodationApiService.class);
        availabilityListLiveData.setValue(availabilityList);
    }

    public LiveData<List<Availability>> getAvailabilityListLiveData() {
        return availabilityListLiveData;
    }

    public void removeAvailability(Availability availability) {
        availabilityList.remove(availability);
        availabilityListLiveData.setValue(new ArrayList<>(availabilityList));
    }

    public void addAvailability(Availability availability) {
        availabilityList.add(availability);
        availabilityListLiveData.setValue(new ArrayList<>(availabilityList));
    }

    public void uploadImages(List<Uri> imageUris, Context context, Callback<List<String>> callback) {
        List<MultipartBody.Part> imageParts = getMultipartBodyPartsFromUris(imageUris, context);
        Call<List<String>> uploadCall = apiService.uploadImages(imageParts);
        uploadCall.enqueue(callback);
    }

    public void createNewAccommodation(AccommodationCreateDto dto, Callback<AccommodationDetailsDto> callback) {
        Call<AccommodationDetailsDto> call = apiService.createNewAccommodation(dto);
        call.enqueue(callback);
    }

    private List<MultipartBody.Part> getMultipartBodyPartsFromUris(List<Uri> imageUris, Context context) {
        List<MultipartBody.Part> parts = new ArrayList<>();

        for (Uri imageUri : imageUris) {
            String filePath = getPathFromUri(context, imageUri);
            if (filePath != null) {
                File file = new File(filePath);

                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

                MultipartBody.Part body = MultipartBody.Part.createFormData("images", file.getName(), requestFile);

                parts.add(body);
            }
        }

        return parts;
    }

    private String getPathFromUri(Context context, Uri uri) {
        String result = null;
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                result = cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }
}