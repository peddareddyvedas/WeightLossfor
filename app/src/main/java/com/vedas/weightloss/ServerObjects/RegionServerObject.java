package com.vedas.weightloss.ServerObjects;

import com.vedas.weightloss.Models.CountryList;

import java.util.ArrayList;

/**
 * Created by Rise on 05/07/2018.
 */

public class RegionServerObject {


    private String status;

    public String getStatus() { return this.status; }

    public void setStatus(String status) { this.status = status; }

    private ArrayList<CountryList> countryList;

    public ArrayList<CountryList> getCountryList() { return this.countryList; }

    public void setCountryList(ArrayList<CountryList> countryList) { this.countryList = countryList; }

    private String nextPageToken;

    public String getNextPageToken() { return this.nextPageToken; }

    public void setNextPageToken(String nextPageToken) { this.nextPageToken = nextPageToken; }

    private boolean canLoadMore;

    public boolean getCanLoadMore() { return this.canLoadMore; }

    public void setCanLoadMore(boolean canLoadMore) { this.canLoadMore = canLoadMore; }
}
