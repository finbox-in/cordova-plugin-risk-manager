package in.finbox.risk;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.support.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import in.finbox.mobileriskmanager.FinBox;
import android.text.TextUtils;
import java.util.concurrent.TimeUnit;

/**
 * FinBox Risk Manager Class
 */
public class FinBoxRiskManager extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(action.equals("createUser")) {
            String apiKey = args.getString(0);
            String customerId = args.getString(1);
            if(TextUtils.isEmpty(apiKey) || TextUtils.isEmpty(customerId)) {
                callbackContext.error("Api Key or customerId cannot be empty");
            } else {
                this.createUser(apiKey, customerId, callbackContext);
            }
            return true;
        } else if(action.equals("startPeriodicSync")) {
            int duration = args.getInt(0);
            this.startPeriodicSync(duration);
            return true;
        }else if(action.equals("stopPeriodicSync")) {
            this.stopPeriodicSync();
            return true;
        }else if(action.equals("resetUserData")) {
            this.resetUserData();
            return true;
        }
        return false;
    }

    /**
     * FinBox create User function. This is used to create a new instance of the user and also to make sure all the
     * syncs are running periodically. Makeu sure this function is called on every app start
     * 
     * @param apiKey The Api Key that is given to authorize valid user
     * @param customerId The name of the user for which profiling will be done
     * @param callbackContext Callback to the cordova app
     */
    private void createUser(@NonNull String apiKey, @NonNull String customerId, CallbackContext callbackContext) {
        FinBox.createUser(apiKey, customerId, new FinBox.FinBoxAuthCallback() {
            @Override
            public void onSuccess(@NonNull String s) {
                callbackContext.success(s);
            }

            @Override
            public void onError(int i) {
                callbackContext.error(i);
            }
        });
    }

    /**
     * Once user is created syncs need to be started. hence call this function when createUser returns success
     * 
     * @param duration Duration in which the sync should occur in background
     */
    private void startPeriodicSync(int duration) {
        FinBox finbox = new FinBox();
        finbox.setRealTime(true);
        finbox.setSyncFrequency(TimeUnit.HOURS.toSeconds(duration));
        finbox.startPeriodicSync();
    }

    /**
     * Function to stop all periodic syncs that are running in the background
     * 
     */
    public void stopPeriodicSync() {
        new FinBox().stopPeriodicSync();
    }

    /**
     * In order to reset all data and fetch all details of the user call this method
     * 
     */
    public void resetUserData() {
        FinBox.resetData();
    }

}
