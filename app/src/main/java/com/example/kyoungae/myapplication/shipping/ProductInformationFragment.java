package com.example.kyoungae.myapplication.shipping;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.databinding.FragmentProductInformationBinding;
import com.example.kyoungae.myapplication.databinding.ListItemShippingProductBinding;
import com.example.kyoungae.myapplication.event.MessageEvent;
import com.example.kyoungae.myapplication.lib.HeaderAndFooterRecyclerViewAdapter;
import com.example.kyoungae.myapplication.lib.RecyclerViewUtils;
import com.example.kyoungae.myapplication.shipping.model.AddressModel;
import com.example.kyoungae.myapplication.shipping.model.CategoryModel;
import com.example.kyoungae.myapplication.shipping.model.ShippingProductModel;
import com.example.kyoungae.myapplication.view.NextFooter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;

import static org.greenrobot.eventbus.EventBus.TAG;

public class ProductInformationFragment extends Fragment implements Serializable, View.OnClickListener {

    private FragmentProductInformationBinding binding;
    private boolean isTextWatcher = true;
    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter;
    private static ArrayList<CategoryModel> mCategoryList;
    private static String mMemberName;
    private BottomSheetBehavior mBottomSheetBehavior;
    private ArrayList<AddressModel> mGetAddressModels;

    //스크롤 방향 체크 변수
    public static ProductInformationFragment newInstance(ArrayList<CategoryModel> categoryList, String name) {
        ProductInformationFragment fragment = new ProductInformationFragment();
        mCategoryList = categoryList;
        mMemberName = name;
        Log.d(TAG, "newInstance: " + mMemberName);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        Log.d(TAG, "onSaveInstanceState: 호출");
//
//        outState.putSerializable("listData", mProductModelHashMap);
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_information, container, false);
        binding = DataBindingUtil.bind(view);

//        ArrayList<ShippingProductModel> mProductModels = ShippingActivity.sProductModelList;

        ListAdapter listAdapter = new ListAdapter(getActivity(), ShippingActivity.sProductModelList, Glide.with(ProductInformationFragment.this));
        mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(listAdapter);

        binding.recyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(manager);

        RecyclerViewUtils.setFooterView(binding.recyclerView, new NextFooter(getActivity()));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
//        if (event instanceof OrderListDataEvent) {
//            mHeaderAndFooterRecyclerViewAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.get_order_history_button:
////                FragmentManager fm = getActivity().getSupportFragmentManager();
////                GetOrderHistoryFragmentDialog dialogFragment = new GetOrderHistoryFragmentDialog();
////                dialogFragment.show(fm, "get_order_history");
//                CommonLib.subActivityIntent(getActivity(),GetOrderHistoryFragment.newInstance());
//                break;
        }
    }

    public class ListAdapter extends RecyclerView.Adapter {
        private int mPosition;
        private LayoutInflater mLayoutInflater;
        private ArrayList<ShippingProductModel> list;
        private final RequestManager glide;

        public ListAdapter(Context context, ArrayList<ShippingProductModel> list, RequestManager glide) {
            this.list = list;
            mLayoutInflater = LayoutInflater.from(context);
            this.glide = glide;

        }

        public void setData(ArrayList<ShippingProductModel> list) {
            this.list = list;
            notifyDataSetChanged();
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.list_item_shipping_product, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            ViewHolder viewHolder = (ViewHolder) holder;

            if (list.get(position) != null) {
                if (list.get(position).getImageUrl() != null) {
                    Log.d("onBindViewHolder: ", "ddddddddd");
//                    String url = list.get(position).getImageUrl().replace("//","");
//                    Glide.with(ProductInformationFragment.this).load(list.get(position).getImageUrl()).centerCrop().into(viewHolder.listBinding.image);
                    glide.load(list.get(position).getImageUrl())
                            .centerCrop()
                            .into(viewHolder.listBinding.image);
                }
                int index = position + 1;
                viewHolder.listBinding.productsIndex.setText("상품정보 " + index);
                viewHolder.listBinding.shoppingMallEdit.setText(list.get(position).getShoppingMall());
                viewHolder.listBinding.orderNumberEdit.setText(list.get(position).getOrderNumber());
                viewHolder.listBinding.buyerEdit.setText(list.get(position).getBuyer());
                if (list.get(position).getCategoryItem() != null) {
                    viewHolder.listBinding.categoryText.setText(list.get(position).getCategoryItem().getKo_name());
                }
                viewHolder.listBinding.productsNameText.setText(list.get(position).getProductName());
                viewHolder.listBinding.trackingNumberEdit.setText(list.get(position).getTrackingNumber());
                viewHolder.listBinding.priceEdit.setText(list.get(position).getPrice());
                Log.d(TAG, "onBindViewHolder: "+list.get(position).getPrice());
                viewHolder.listBinding.quantityEdit.setText(list.get(position).getQuantity());
                viewHolder.listBinding.colorEdit.setText(list.get(position).getColor());
                viewHolder.listBinding.sizeEdit.setText(list.get(position).getSize());
                viewHolder.listBinding.goodsUrlEdit.setText(list.get(position).getGoodsUrl());
                viewHolder.listBinding.imageUrlEdit.setText(list.get(position).getImageUrl());
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher {

            public ListItemShippingProductBinding listBinding;
            private boolean isNullValue;

            public ViewHolder(View itemView) {
                super(itemView);
                listBinding = DataBindingUtil.bind(itemView);

                setButtonView();
                if (isTextWatcher) {
                    setTextWatcher();
                }
            }

            public void setButtonView() {
                listBinding.getOrderListButton.setOnClickListener(this);
                listBinding.copyButton.setOnClickListener(this);
                listBinding.addButton.setOnClickListener(this);
                listBinding.deleteButton.setOnClickListener(this);
                listBinding.categorySelect.setOnClickListener(this);
            }

            public void setTextWatcher() {

                listBinding.orderNumberEdit.addTextChangedListener(this);
                listBinding.buyerEdit.addTextChangedListener(this);
                listBinding.productsNameText.addTextChangedListener(this);
                listBinding.trackingNumberEdit.addTextChangedListener(this);
                listBinding.priceEdit.addTextChangedListener(this);
                listBinding.quantityEdit.addTextChangedListener(this);
                listBinding.colorEdit.addTextChangedListener(this);
                listBinding.sizeEdit.addTextChangedListener(this);
                listBinding.goodsUrlEdit.addTextChangedListener(this);
                listBinding.imageUrlEdit.addTextChangedListener(this);

            }

            @Override
            public void onClick(View v) {
                mPosition = RecyclerViewUtils.getAdapterPosition(binding.recyclerView, ViewHolder.this);
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    view.clearFocus();
                }
                switch (v.getId()) {
                    case R.id.get_order_list_button:
                        Intent intent = new Intent(getActivity(), OrderListActivity.class);
                        intent.putExtra("member_name", mMemberName);
                        getActivity().startActivity(intent);

                        break;
                    case R.id.copy_button:
                        list.add(new ShippingProductModel(list.get(mPosition).getOrderNumber(), list.get(mPosition).getBuyer(), list.get(mPosition).getCategoryItem(),
                                list.get(mPosition).getProductName(), list.get(mPosition).getTrackingNumber(), list.get(mPosition).getPrice(), list.get(mPosition).getQuantity(),
                                list.get(mPosition).getColor(), list.get(mPosition).getSize(), list.get(mPosition).getGoodsUrl(), list.get(mPosition).getImageUrl()));
//                        for (int i = 0; i < list.size(); i++) {
//                            TextUtils.isEmpty(list.get(i).getOrderNumber())
//                        }
//                        notifyDataSetChanged();
//                        binding.recyclerView.scrollToPosition(ScrollView.FOCUS_DOWN);
                        notifyItemInserted(list.size());
                        break;
                    case R.id.add_button:
                        list.add(new ShippingProductModel("", mMemberName, new CategoryModel(), "", "", "", "", "", "", "", ""));

//                        binding.recyclerView.scrollToPosition(ScrollView.FOCUS_DOWN);
                        notifyItemInserted(list.size());

//                        itemNullCheck();
//                        notifyDataSetChanged();
                        break;
                    case R.id.delete_button:
                        if (list.size() > 1) {
                            list.remove(mPosition);
//                            notifyItemRemoved(mPosition);

//                            Animation animation = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
//                            listBinding.cardView.startAnimation(animation);
                            notifyDataSetChanged();
                            int productPosition = mPosition + 1;
                            Toast.makeText(getActivity(), "상품정보" + productPosition + "이(가) 삭제되었습니다.", Toast.LENGTH_SHORT).show();
//                            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();


//                            notifyItemRemoved(mPosition);
//                            notifyItemChanged(mPosition);
                        }

                        break;
                    case R.id.category_select:  //품목 고르기
                        PopupMenu popupMenu = new PopupMenu(getActivity(), v);//v는 클릭된 뷰를 의미
                        Menu menu = popupMenu.getMenu();
                        for (int i = 0; i < mCategoryList.size(); i++) {
                            menu.add(0, i, 0, mCategoryList.get(i).getKo_name());
                        }
                        popupMenu.getMenuInflater().inflate(R.menu.popup_category, menu);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int clickId = item.getItemId();
                                listBinding.categoryText.setText(item.getTitle());
                                listBinding.productsNameText.setText(mCategoryList.get(clickId).getEn_name());
                                list.get(mPosition).setCategoryItem(new CategoryModel(mCategoryList.get(clickId).getId(), mCategoryList.get(clickId).getKo_name(),
                                        mCategoryList.get(clickId).getEn_name()));  //리스트에 카테고리항목 넣기
                                list.get(mPosition).setProductName(mCategoryList.get(clickId).getEn_name());  //리스트에 상품 이름 넣기
                                return true;
                            }
                        });
                        popupMenu.show();
                        break;
//                    case R.id.category_select:
//                        ArrayList<SearchCategoryModel> searchCategoryList = new ArrayList<>();
//                        for (int i = 0; i < mCategoryList.size(); i++) {
//                            searchCategoryList.add(new SearchCategoryModel(mCategoryList.get(i).getKo_name()));
//                        }
//
//                        new SimpleSearchDialogCompat(getActivity(), "품목 검색",
//                                "찾으실 품목을 입력하세요.", null, searchCategoryList,
//                                new SearchResultListener<SearchCategoryModel>() {
//                                    @Override
//                                    public void onSelected(BaseSearchDialogCompat dialog,
//                                                           SearchCategoryModel item, int position) {
//                                        Toast.makeText(getActivity(), item.getKoCategoryName(),
//                                                Toast.LENGTH_SHORT).show();
//                                        dialog.dismiss();
//                                    }
//                                }).show();
//
//                        break;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                saveText(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            public void saveText(String text) {
                mPosition = RecyclerViewUtils.getAdapterPosition(binding.recyclerView, ViewHolder.this);
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    switch (view.getId()) {
                        case R.id.order_number_edit:
                            list.get(mPosition).setOrderNumber(text);
                            break;
                        case R.id.buyer_edit:
                            list.get(mPosition).setBuyer(text);
                            break;
                        case R.id.products_name_edit:
                            list.get(mPosition).setProductName(text);
                            break;
                        case R.id.tracking_number_edit:
                            list.get(mPosition).setTrackingNumber(text);
                            break;
                        case R.id.price_edit:
                            list.get(mPosition).setPrice(text);
                            break;
                        case R.id.quantity_edit:
                            list.get(mPosition).setQuantity(text);
                            break;
                        case R.id.color_edit:
                            list.get(mPosition).setColor(text);
                            break;
                        case R.id.size_edit:
                            list.get(mPosition).setSize(text);
                            break;
                        case R.id.goods_url_edit:
                            list.get(mPosition).setGoodsUrl(text);
                            break;
                        case R.id.image_url_edit:
                            list.get(mPosition).setImageUrl(text);
                            break;
                    }
                }
            }

            public void itemNullCheck() {
                isNullValue = false;

                for (int i = 0; i < list.size(); i++) {
                    String nullValue = list.get(i).itemNullCheck(true);
                    if (!isNullValue) {
                        if (!nullValue.equals("ok")) {  //nullValue 값이 ok가 아닐때
                            isNullValue = true;
                            binding.recyclerView.scrollToPosition(i);
//                            if(binding.recyclerView.findViewHolderForItemId(i).itemView.getId()==R.id.tracking_number_edit){
//                                listBinding.trackingNumberEdit.setFocusableInTouchMode(true);
//                                listBinding.trackingNumberEdit.requestFocus();
//                            }

                            Toast.makeText(getActivity(), nullValue + "은 필수 입력항목입니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }
}
