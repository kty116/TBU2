package com.example.kyoungae.myapplication.shipping.dialog;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kyoungae.myapplication.R;
import com.example.kyoungae.myapplication.common.CommonLib;
import com.example.kyoungae.myapplication.databinding.DialogGetAddressBinding;
import com.example.kyoungae.myapplication.databinding.ListItemDialogGetAddressBinding;
import com.example.kyoungae.myapplication.dto.KeyDTO;
import com.example.kyoungae.myapplication.event.RecieverInformationSelectAddressEvent;
import com.example.kyoungae.myapplication.lib.HeaderAndFooterRecyclerViewAdapter;
import com.example.kyoungae.myapplication.lib.RecyclerViewUtils;
import com.example.kyoungae.myapplication.lib.TheBayRestClient;
import com.example.kyoungae.myapplication.login.LoginActivity;
import com.example.kyoungae.myapplication.shipping.model.AddressModel;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static org.greenrobot.eventbus.EventBus.TAG;

public class GetAddressDialogFragment extends DialogFragment {

    private DialogGetAddressBinding binding;

//                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

    public static GetAddressDialogFragment newInstance() {
        GetAddressDialogFragment fragment = new GetAddressDialogFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.dialog_get_address, container, false);
        binding = DataBindingUtil.bind(view);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RequestParams params = new RequestParams();

        KeyDTO keyInfo = CommonLib.getKeyInfo(getContext());

        if (keyInfo != null) {
            params.put("AuthKey", keyInfo.getAuthKey());
            params.put("MemCode", keyInfo.getMemberCode());
        }
        // TODO: 2017-10-19 page name 수정
        params.put("PageNm", "배송대행 신청> 주소록 가져오기");
        params.put("AppVer", Build.MODEL);
        params.put("ModelNo", Build.VERSION.RELEASE);

        try {
            getHttp("Acting/DlvrAddr_S.php", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getHttp(String relativeUrl, RequestParams params) throws JSONException {

        TheBayRestClient.post(relativeUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
//                EventBus.getDefault().post(new ProgressBarEvent(true));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // 받아온 JSONObject 자료 처리

                Log.d("onHomeSuccess: ", response.toString());

                // TODO: 2017-10-19 데이터 받은거 수정

                ArrayList<AddressModel> getAddressModels = new ArrayList<>();

                try {
                    String error = response.getString("RstNo");
                    if (error.equals("0")) {
                        JSONArray array = response.getJSONArray("MemAddr");
                        Log.d("onHomeSuccess: ", array.toString());
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            getAddressModels.add(new AddressModel(object.getString("AddrSeq"), object.getString("AdrsKr"),
                                    object.getString("AdrsEn"), object.getString("Zip"), object.getString("Addr1"),
                                    object.getString("Addr2"), object.getString("Addr1En"), object.getString("Addr2En")
                                    , object.getString("MobNo"), object.getString("RrnNo"), object.getString("RrnCd"),
                                    object.getString("MainYn")));
//                            Log.d("onHomeSuccess: ",homeModelList.toString());
//                            binding.addressText.setText(getAddressModels.toString());
                        }
//                        binding.parentLayout.setVisibility(View.VISIBLE);

                        setRecyclerView(getAddressModels);
                    } else {
                        Toast.makeText(getContext(), "로그인 정보가 틀립니다.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                // 끝나면 호출 되는 메소드
//                EventBus.getDefault().post(new ProgressBarEvent(false));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // 통신 실패시 호출 되는 메소드
                Toast.makeText(getContext(), "서버와 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.d("onFailure: ", throwable.toString());
            }
        });
    }

    public void setRecyclerView(ArrayList<AddressModel> list){

        ListAdapter listAdapter = new ListAdapter(getActivity(), list);
        HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(listAdapter);

        binding.recyclerView.setAdapter(mHeaderAndFooterRecyclerViewAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(manager);

    }

    public class ListAdapter extends RecyclerView.Adapter {
        private int mPosition;
        private LayoutInflater mLayoutInflater;
        private ArrayList<AddressModel> list;

        public ListAdapter(Context context, ArrayList<AddressModel> list) {
            this.list = list;
            mLayoutInflater = LayoutInflater.from(context);

        }

        public void setData(ArrayList<AddressModel> list) {
            this.list = list;
            notifyDataSetChanged();
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mLayoutInflater.inflate(R.layout.list_item_dialog_get_address, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            final ViewHolder viewHolder = (ViewHolder) holder;
            if (list.get(position) != null) {
                viewHolder.listBinding.koNameText.setText(list.get(position).getKoName());
                viewHolder.listBinding.enNameText.setText(list.get(position).getEnName());
                viewHolder.listBinding.customsClearanceText.setText(list.get(position).getCcNumber());
                viewHolder.listBinding.phoneText.setText(list.get(position).getPhoneNumber());
                viewHolder.listBinding.koAddressText.setText(list.get(position).getPost() +"\n"+ list.get(position).getKoAddress()+" "+list.get(position).getKoDetailAddress());
                viewHolder.listBinding.enAddressText.setText(list.get(position).getPost() +"\n"+ list.get(position).getEnAddress()+" "+list.get(position).getEnDetailAddress());
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public ListItemDialogGetAddressBinding listBinding;

            public ViewHolder(View itemView) {
                super(itemView);
                listBinding = DataBindingUtil.bind(itemView);

                setView();
            }

            private void setView() {

                listBinding.selectButton.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                mPosition = RecyclerViewUtils.getAdapterPosition(binding.recyclerView, ViewHolder.this);
                EventBus.getDefault().post(new RecieverInformationSelectAddressEvent(list.get(mPosition)));
                Log.d(TAG, "onClick: "+list.get(mPosition).toString());

                getActivity().getSupportFragmentManager().beginTransaction().remove(GetAddressDialogFragment.this).commit();
            }
        }
    }
}
