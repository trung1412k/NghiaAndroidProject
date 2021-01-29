/*
 * Copyright (c) 2017. http://hiteshsahu.com- All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * If you use or distribute this project then you MUST ADD A COPY OF LICENCE
 * along with the project.
 *  Written by Hitesh Sahu <hiteshkrsahu@Gmail.com>, 2017.
 */

package com.hitesh_sahu.retailapp.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
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
import com.hitesh_sahu.retailapp.view.adapter.ProductsInCategoryPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductOverviewFragment extends Fragment {

    // SimpleRecyclerAdapter adapter;
    private KenBurnsView header;
    private Bitmap bitmap;
    private Toolbar mToolbar;
    private ViewPager viewPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_category_details,
                container, false);

        getActivity().setTitle("Products");

        // Simulate Web service calls
//        FakeWebServer.getFakeWebServer().getAllProducts(
//                AppConstants.CURRENT_CATEGORY);

        // TODO We Can use Async task But pallete creation is problemitic job
        // will
        // get back to it later

        // new ProductLoaderTask(null, getActivity(), viewPager, tabLayout);

        // Volley can be used here very efficiently but Fake JSON creation is
        // time consuming Leain it now

        viewPager = (ViewPager) view.findViewById(R.id.htab_viewpager);

        collapsingToolbarLayout = (CollapsingToolbarLayout) view
                .findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        header = (KenBurnsView) view.findViewById(R.id.htab_header);
        header.setImageResource(R.drawable.header);

        tabLayout = (TabLayout) view.findViewById(R.id.htab_tabs);

        mToolbar = (Toolbar) view.findViewById(R.id.htab_toolbar);
        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).setSupportActionBar(mToolbar);
        }

        if (mToolbar != null) {
            ((ECartHomeActivity) getActivity()).getSupportActionBar()
                    .setDisplayHomeAsUpEnabled(true);

            mToolbar.setNavigationIcon(R.drawable.ic_drawer);

        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ECartHomeActivity) getActivity()).getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        setUpUi();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    Utils.switchContent(R.id.frag_container,
                            Utils.HOME_FRAGMENT,
                            ((ECartHomeActivity) (getContext())),
                            AnimationType.SLIDE_RIGHT);

                }
                return true;
            }
        });

        return view;
    }

    private void setUpUi() {

        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

        bitmap = BitmapFactory
                .decodeResource(getResources(), R.drawable.header);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                int vibrantDarkColor = palette
                        .getDarkVibrantColor(R.color.primary_700);
                collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                collapsingToolbarLayout
                        .setStatusBarScrimColor(vibrantDarkColor);
            }
        });

        tabLayout
                .setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {

                        viewPager.setCurrentItem(tab.getPosition());

                        header.setImageResource(R.drawable.header);

                        bitmap = BitmapFactory.decodeResource(
                                getResources(), R.drawable.header);

                        Palette.from(bitmap).generate(
                                new Palette.PaletteAsyncListener() {
                                    @SuppressWarnings("ResourceType")
                                    @Override
                                    public void onGenerated(Palette palette) {

                                        int vibrantColor = palette
                                                .getVibrantColor(R.color.primary_500);
                                        int vibrantDarkColor = palette
                                                .getDarkVibrantColor(R.color.primary_700);
                                        collapsingToolbarLayout
                                                .setContentScrimColor(vibrantColor);
                                        collapsingToolbarLayout
                                                .setStatusBarScrimColor(vibrantDarkColor);
                                    }
                                });
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

    }

    private void setupViewPager(ViewPager viewPager) {
        ProductsInCategoryPagerAdapter adapter = new ProductsInCategoryPagerAdapter(
                getActivity().getSupportFragmentManager());
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(AppConstants.CURRENT_CATEGORY));
//        params.put("keyword", keyword);
//        params.put("brand_id", String.valueOf(brandId));
//        params.put("group_id", String.valueOf(groupId));
//        params.put("offset", String.valueOf(offset));
//        params.put("limit", String.valueOf(limit));
//        params.put("sort", String.valueOf(sort));
        APIInterface api = AppClient.client().create(APIInterface.class);
        Call<ResponseList<Product>> call = api.products(params);

        call.enqueue(new Callback<ResponseList<Product>>() {

            @Override
            public void onResponse(Call<ResponseList<Product>> call, Response<ResponseList<Product>> response) {
                if (response.body() != null) {

                    ConcurrentHashMap<String, ArrayList<Product>> productMap = new ConcurrentHashMap<String, ArrayList<Product>>();
                    productMap.put("Sản phẩm", response.body().getData());
                    CenterRepository.getCenterRepository().setMapOfProductsInCategory(productMap);
                } else {
                    Log.d("onResponse product:", "ccc");
                    FakeWebServer.getFakeWebServer().addCategory();
                }
//                FakeWebServer.getFakeWebServer().getAllProducts(
//                        AppConstants.CURRENT_CATEGORY);
                Set<String> keys = CenterRepository.getCenterRepository().getMapOfProductsInCategory()
                        .keySet();
                for (String string : keys) {
                    adapter.addFrag(new ProductListFragment(string), string);
                }

                viewPager.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ResponseList<Product>> call, Throwable t) {
                Log.d("doInBackground error", t.getMessage());
                FakeWebServer.getFakeWebServer().addCategory();
            }
        });
    }
}