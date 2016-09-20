package com.dgnt.quickTournamentMaker.util;

import com.google.android.gms.ads.AdRequest;

/**
 * Created by Andrew on 11/7/2015.
 */
public class AdsUtil {

    public static AdRequest buildAdRequestWithTestDevices(){
        //Make sure you add test devices as more are being used.
        //1. Run the app as normal
        //2. Go to logcat and put adRequest as the filter string
        //3. Look for this msg "To get test ads on this device, call adRequest.addTestDevice("D9XXXXXXXXXXXXXXXXXXXXXXXXXXXXX")"
        //4. Get the hash id and add the test device with that id.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("20F25CE6BD544C548D75E79DFE95B078")  //Andrew's Galaxy Prime
                .build();

        return adRequest;
    }
}
