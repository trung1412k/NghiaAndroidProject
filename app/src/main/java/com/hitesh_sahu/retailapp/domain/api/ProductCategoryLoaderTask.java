/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.hitesh_sahu.retailapp.domain.api;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.hitesh_sahu.retailapp.R;
import com.hitesh_sahu.retailapp.domain.helper.APIInterface;
import com.hitesh_sahu.retailapp.domain.helper.AppClient;
import com.hitesh_sahu.retailapp.domain.mock.FakeWebServer;
import com.hitesh_sahu.retailapp.model.CenterRepository;
import com.hitesh_sahu.retailapp.model.entities.Product;
import com.hitesh_sahu.retailapp.model.entities.ProductCategoryModel;
import com.hitesh_sahu.retailapp.model.entities.ResponseList;
import com.hitesh_sahu.retailapp.util.AppConstants;
import com.hitesh_sahu.retailapp.util.Utils;
import com.hitesh_sahu.retailapp.util.Utils.AnimationType;
import com.hitesh_sahu.retailapp.view.activities.ECartHomeActivity;
import com.hitesh_sahu.retailapp.view.adapter.CategoryListAdapter;
import com.hitesh_sahu.retailapp.view.adapter.CategoryListAdapter.OnItemClickListener;
import com.hitesh_sahu.retailapp.view.fragment.ProductOverviewFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The Class ImageLoaderTask.
 */
public class ProductCategoryLoaderTask extends AsyncTask<String, Void, Void> {

    private static final int NUMBER_OF_COLUMNS = 2;
    private Context context;
    private RecyclerView recyclerView;

    public ProductCategoryLoaderTask(RecyclerView listView, Context context) {

        this.recyclerView = listView;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        if (null != ((ECartHomeActivity) context).getProgressBar())
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.VISIBLE);

    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (null != ((ECartHomeActivity) context).getProgressBar())
            ((ECartHomeActivity) context).getProgressBar().setVisibility(
                    View.GONE);

        if (recyclerView != null) {
            CategoryListAdapter simpleRecyclerAdapter = new CategoryListAdapter(
                    context);

            recyclerView.setAdapter(simpleRecyclerAdapter);

            simpleRecyclerAdapter
                    .SetOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {

                            AppConstants.CURRENT_CATEGORY = CenterRepository.getCenterRepository().getListOfCategory().get(position).getID();

                            Utils.switchFragmentWithAnimation(
                                    R.id.frag_container,
                                    new ProductOverviewFragment(),
                                    ((ECartHomeActivity) context), null,
                                    AnimationType.SLIDE_LEFT);

                        }
                    });
        }

    }

    @Override
    protected Void doInBackground(String... params) {

        APIInterface api = AppClient.client().create(APIInterface.class);
        Call<ResponseList<ProductCategoryModel>> call = api.categories();

        call.enqueue(new Callback<ResponseList<ProductCategoryModel>>() {

            @Override
            public void onResponse(Call<ResponseList<ProductCategoryModel>> call, Response<ResponseList<ProductCategoryModel>> response) {

                if (response.body() != null) {
                    CenterRepository.getCenterRepository().setListOfCategory(response.body().getData());
                } else {
                    FakeWebServer.getFakeWebServer().addCategory();
                }
            }

            @Override
            public void onFailure(Call<ResponseList<ProductCategoryModel>> call, Throwable t) {
                Log.d("doInBackground error", t.getMessage());
                FakeWebServer.getFakeWebServer().addCategory();
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}