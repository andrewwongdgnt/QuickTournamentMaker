package com.dgnt.quickTournamentMaker.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dgnt.quickTournamentMaker.R;
import com.dgnt.quickTournamentMaker.util.inappbilling.IabBroadcastReceiver;
import com.dgnt.quickTournamentMaker.util.inappbilling.IabHelper;
import com.dgnt.quickTournamentMaker.util.inappbilling.IabResult;
import com.dgnt.quickTournamentMaker.util.inappbilling.Inventory;
import com.dgnt.quickTournamentMaker.util.inappbilling.Purchase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Owner on 5/13/2016.
 */
public abstract class InAppBillingActivity extends AppCompatActivity implements IabBroadcastReceiver.IabBroadcastListener {

    private boolean premium = false;

    public boolean isPremium() {
        return premium;
//        return true;
//        return false;
    }


    static final String SKU_PREMIUM = "qtm_premium";

    static final String TAG = "QuickTournamentMaker";

    // (arbitrary) request code for the purchase flow
    static final int REQUEST_PURCHASE_FLOW = 10001;

    // request code for picking user for IAP
    static final int REQUEST_CODE_EMAIL = 666;

    // The helper object
    IabHelper iabHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver iabBroadcastReceiver;

    //state
    protected boolean iabSetupFailed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String base64EncodedPublicKey = buildBase64PublicKey();

        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        iabHelper = new IabHelper(this, base64EncodedPublicKey);
        iabHelper.enableDebugLogging(true);

        Log.d(TAG, "Starting setup.");
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");
                iabSetupFailed = true;
                if (!result.isSuccess()) {

                    // Oh noes, there was a problem.
                    complain(result, silentComplainOnStartUp());
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (iabHelper == null) return;

                iabSetupFailed = false;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                iabBroadcastReceiver = new IabBroadcastReceiver(InAppBillingActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(iabBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                iabHelper.queryInventoryAsync(inventoryListener);
            }
        });


    }

    protected boolean silentComplainOnStartUp() {
        return true;
    }

    //apparently we should obfuscate as much as we can
    private static String buildBase64PublicKey() {
        return keyPart1() + keyPart2() + keyPart3();
    }

    private static String keyPart1() {
        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkK5PKirQ61DP/LPtxVUY6q8SQ7fFpod7LuRcHuF/KgIAtUSSHj2AvONdC0Ua2CUUX6oiypT7d6kFcT";
    }

    private static String keyPart2() {
        return "F9juUFd5Z7IBweVazNdgwbsYG/l0YUC2+nDghiQGRRHrUC/2E9Sag6iE4R2qEwiigFIK+Y/yT8p/ejVTpBBppib+ZwQKo3ehMCs6stNOaFcmXWAUkv/SwbGdLu";
    }

    private static String keyPart3() {
        return "PyWSqRxGdDtahn/kjtMVLnPBubQ+ZYZm8UURy5ND1l4kRzLRHNzCU6Id1j5vuiJ45heebXZDnylLZcHqP0cMsSARP1DphKLcqabaEyw4bVzZKNnAYA/LjMc5Jh" + keyPart3_2();
    }

    private static String keyPart3_2() {
        return "6PM22z417Yn+HhfmvapwIDAQAB  ";
    }


    protected void upgrade() {
        if (isPremium()) return;
        handleUpgradingToPremium();
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener inventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (iabHelper == null) {
                handleAfterIABResolution();
                return;
            }
            // Is it a failure?
            if (result.isFailure()) {
                complain(result);
                handleAfterIABResolution();
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            premium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (!isPremium() ? "PREMIUM" : "NOT PREMIUM"));


            handleAfterIABResolution();
            setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        iabHelper.queryInventoryAsync(inventoryListener);
    }

    // User clicked the "Upgrade to Premium" button.
    public void handleUpgradingToPremium() {
        Log.d(TAG, "Launching purchase flow for upgrade.");
        setWaitScreen(true);

        try {
            final String payload = getHash("FourMonkeysInSuits ");
            iabHelper.launchPurchaseFlow(this, SKU_PREMIUM, REQUEST_PURCHASE_FLOW, purchaseFinishedListener, payload);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private static String getHash(final String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        if (requestCode == REQUEST_PURCHASE_FLOW) {
            if (iabHelper == null) return;

            // Pass on the activity result to the helper for handling
            if (!iabHelper.handleActivityResult(requestCode, resultCode, data)) {
                // not handled, so handle it ourselves (here's where you'd
                // perform any handling of activity results not related to in-app
                // billing...

            } else {
                Log.d(TAG, "onActivityResult handled by IABUtil.");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        boolean payloadVerified = false;
        try {
            payloadVerified = payload.equals(getHash("FourMonkeysInSuits"));
        } catch (NoSuchAlgorithmException e) {

        }

        return payloadVerified;
    }

    IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (iabHelper == null) {
                handleAfterIABResolution();
                return;
            }

            if (result.isFailure()) {
                complain(result);
                setWaitScreen(false);
                handleAfterIABResolution();
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain(getString(R.string.verification_errorMsg));
                setWaitScreen(false);
                handleAfterIABResolution();
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                alert(getString(R.string.upgradeSuccessfulMsg));
                premium = true;
                handleAfterIABResolution();
                setWaitScreen(false);
            }
        }
    };

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (iabBroadcastReceiver != null) {
            unregisterReceiver(iabBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (iabHelper != null) {
            iabHelper.dispose();
            iabHelper = null;
        }

    }

    void complain(String message) {
        complain(message, false);
    }

    void complain(String message, boolean silent) {
        Log.e(TAG, "**** JamWaiver Error: " + message);
        if (!silent)
            alert("Error: " + message);
    }

    void complain(IabResult iabResult) {
        complain(iabResult, false);
    }

    void complain(final IabResult iabResult, boolean silent) {
        Log.e(TAG, "**** JamWaiver Error: " + iabResult);

        if (!silent) {
            switch (iabResult.getResponse()) {
                case IabHelper.BILLING_RESPONSE_RESULT_USER_CANCELED:
                    break;
                case IabHelper.BILLING_RESPONSE_RESULT_SERVICE_UNAVAILABLE:
                    alert(getString(R.string.service_unavailable_errorMsg));
                    break;
                case IabHelper.BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE:
                    alert(getString(R.string.service_unavailable_errorMsg));
                    break;
                case IabHelper.BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE:
                    break;
                case IabHelper.BILLING_RESPONSE_RESULT_DEVELOPER_ERROR:
                    break;
                case IabHelper.BILLING_RESPONSE_RESULT_ERROR:
                    alert(getString(R.string.unknown_errorMsg));
                    break;
                case IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED:
                    alert(getString(R.string.item_already_owned_errorMsg));
                    break;
                case IabHelper.BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED:
                    break;
                case IabHelper.IABHELPER_REMOTE_EXCEPTION:
                    alert(getString(R.string.initialization_errorMsg));
                    break;
                case IabHelper.IABHELPER_BAD_RESPONSE:
                    break;
                case IabHelper.IABHELPER_VERIFICATION_FAILED:
                    alert(getString(R.string.verification_errorMsg));
                    break;
                case IabHelper.IABHELPER_SEND_INTENT_FAILED:
                    break;
                case IabHelper.IABHELPER_USER_CANCELLED:
                    break;
                case IabHelper.IABHELPER_UNKNOWN_PURCHASE_RESPONSE:
                    break;
                case IabHelper.IABHELPER_MISSING_TOKEN:
                    break;
                case IabHelper.IABHELPER_UNKNOWN_ERROR:
                    alert(getString(R.string.unknown_errorMsg));
                    break;
                case IabHelper.IABHELPER_SUBSCRIPTIONS_NOT_AVAILABLE:
                    break;
                case IabHelper.IABHELPER_INVALID_CONSUMPTION:
                    break;
                case IabHelper.IABHELPER_SUBSCRIPTION_UPDATE_NOT_AVAILABLE:
                    break;
            }
        }

    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton(getString(android.R.string.ok), null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    // updates UI to reflect model
    protected void handleAfterIABResolution() {

    }

    protected void handleIfIABSetUpFailed() {
        if (iabSetupFailed)
            handleAfterIABResolution();
    }

    // Enables or disables the "please wait" screen.
    void setWaitScreen(boolean set) {


    }
}
