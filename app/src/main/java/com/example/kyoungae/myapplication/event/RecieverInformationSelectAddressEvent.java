package com.example.kyoungae.myapplication.event;


import com.example.kyoungae.myapplication.shipping.model.AddressModel;

/**
 * Created by kyoungae on 2017-09-14.
 */

public class RecieverInformationSelectAddressEvent implements MessageEvent {
    private AddressModel addressModel;

    public RecieverInformationSelectAddressEvent(AddressModel addressModel) {
        this.addressModel = addressModel;
    }

    public AddressModel getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(AddressModel addressModel) {
        this.addressModel = addressModel;
    }

    @Override
    public String toString() {
        return "RecieverInformationSelectAddressEvent{" +
                "addressModel=" + addressModel +
                '}';
    }
}
