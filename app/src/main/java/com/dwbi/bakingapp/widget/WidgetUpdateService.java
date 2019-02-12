package com.dwbi.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.dwbi.bakingapp.model.Recipe;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WidgetUpdateService extends IntentService {
    private static final String TAG = "PSX";


    private static final String ACTION_UPDATE_WIDGET = "com.dwbi.bakingapp.widget.ACTION_UPDATE_WIDGET";
    private static final String ACTION_UPDATE_WIDGET2 = "com.dwbi.bakingapp.widget.ACTION_UPDATE_WIDGET2";

    // TODO: Rename parameters
    public static final String UPDATE_WIDGET_PARAM1 = "com.dwbi.bakingapp.widget.PARAM1";
    public static final String UPDATE_WIDGET_PARAM2 = "com.dwbi.bakingapp.widget.PARAM2";

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    //--------------------------------------------------------------------------------------------------------------------
    // TODO: Customize helper method
    public static void startWidgetUpdateService(Context context, Recipe recipe) {
        Intent intent = new Intent(context, WidgetUpdateService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        intent.putExtra(WidgetUpdateService.UPDATE_WIDGET_PARAM1, recipe);
        context.startService(intent);
    }
    //--------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                if(intent.hasExtra(WidgetUpdateService.UPDATE_WIDGET_PARAM1)) {


                    Recipe recipe = intent.getParcelableExtra(WidgetUpdateService.UPDATE_WIDGET_PARAM1);
                    Log.d(TAG, "WidgetUpdateService.onHandleIntent recipe-> " + recipe.getName());
                    handleActionUpdateWidget(recipe);
                }
            }
        }
    }
    //--------------------------------------------------------------------------------------------------------------------


    private void handleActionUpdateWidget(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));

        BakingAppWidgetProvider.updateGridWidgets(this, appWidgetManager, appWidgetIds, recipe);
    }
}
