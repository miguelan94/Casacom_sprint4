package com.streamnow.sentmatt.datamodel;

import com.streamnow.sentmatt.interfaces.IMenuPrintable;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * !
 * Created by Miguel Estévez on 2/2/16.
 */
public class LDSessionUser {
    public ArrayList<LDCategory> categories;
    public ArrayList<LDLanguage> availableLanguages;
    public String deviceSessionId;
    public String validUntil;
    public ArrayList<LDService> availableServices;
    public String refreshToken;
    public LDUserInfo userInfo;
    public String urlTest;
    public String accessToken;

    public LDSessionUser(JSONObject o) {
        try {
            if (!o.isNull("categories"))
                this.categories = LDCategory.categoriesFromArray(o.getJSONArray("categories"));
            if (!o.isNull("available_languages"))
                this.availableLanguages = LDLanguage.languagesFromArray(o.getJSONArray("available_languages"));
            if (!o.isNull("deviceSessionId")) this.deviceSessionId = o.getString("deviceSessionId");
            if (!o.isNull("validUntil")) this.validUntil = o.getString("validUntil");
            if (!o.isNull("available_services"))
                this.availableServices = LDService.servicesFromArray(o.getJSONArray("available_services"));
            if (!o.isNull("refresh_token")) this.refreshToken = o.getString("refresh_token");
            if (!o.isNull("user_info"))
                this.userInfo = new LDUserInfo(o.getJSONObject("user_info"));
            if (!o.isNull("url_test")) this.urlTest = o.getString("url_test");
            if (!o.isNull("access_token")) this.accessToken = o.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LDService> getAvailableServicesForCategoryId(String categoryId) {
        ArrayList<LDService> retArray = new ArrayList<>();
        int categoryIdInt = Integer.parseInt(categoryId);

        if (categoryIdInt != 0) {
            for (int i = 0; i < this.availableServices.size(); i++) {
                LDService service = this.availableServices.get(i);
                if (service.categoryId == categoryIdInt && service.usable && !service.type.equals("4")) {
                    retArray.add(service);
                }
            }
        }
        return retArray;
    }

    public ArrayList<? extends IMenuPrintable> getAvailableServicesForSession() {
        ArrayList<IMenuPrintable> retArray = new ArrayList<>();

        for (int i = 0; i < this.availableServices.size(); i++) {
            LDService service = this.availableServices.get(i);
            if (service.categoryId == 0 && service.active.equals("1") && service.availableInApp.equals("1") &&
                    service.usable && (service.deviceType.equals("2") || service.deviceType.equals("3")) && !service.type.equals("4")) {
                retArray.add(service);
            }
        }

        ArrayList<LDCategory> categories_aux = new ArrayList<>();
        int cont = 0;
        for (LDCategory ldc: this.categories) {
            for (LDService lds: getAvailableServicesForCategoryId(ldc.id)) {
                if(lds.type.equals("4")){
                    cont++;
                }
            }
            if(cont == getAvailableServicesForCategoryId(ldc.id).size()){
                //retArray.remove(ldc);
                categories_aux.add(ldc);
            }
        }
        for (LDCategory ldc: categories_aux) {
            this.categories.remove(ldc);
        }
        retArray.addAll(this.categories);

        return retArray;
    }
    public ArrayList<LDCategory> getCategories(){
        return this.categories;
    }
}
