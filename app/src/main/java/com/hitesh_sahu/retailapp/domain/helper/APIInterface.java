package com.hitesh_sahu.retailapp.domain.helper;

import com.hitesh_sahu.retailapp.model.entities.Product;
import com.hitesh_sahu.retailapp.model.entities.ProductCategoryModel;
import com.hitesh_sahu.retailapp.model.entities.ResponseList;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface APIInterface {

    @GET(AppClient.MIDDLE_URL + "api/categories")
    Call<ResponseList<ProductCategoryModel>> categories();

    @GET(AppClient.MIDDLE_URL + "api/products")
    Call<ResponseList<Product>> products(@QueryMap Map<String, String> options);

//    @POST(AppClient.MIDDLE_UAARL + "api/order")
//    Call<ResponseList<Invoice>> getOrders(@HeaderMap Map<String, String> headers);
}
