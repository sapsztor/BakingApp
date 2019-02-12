package com.dwbi.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.dwbi.bakingapp.model.Recipe;
import com.dwbi.bakingapp.ui.RecipeListActivity;
import com.dwbi.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */

public class BakingAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "PSXWIDGET";

    public static Recipe mRecipe;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);

        Intent intent = new Intent(context, RecipeListActivity.class );
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_container_layout);

        views.setTextViewText(R.id.widget_tvrecipename, widgetText);

        views.setOnClickPendingIntent(R.id.widget_tvrecipename, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    //--------------------------------------------------------------------------------------------------------------------------------------------

    public static void updateAppWidget3(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {
        Log.d(TAG, "BakingAppWidgetProvider.updateAppWidget3 recipe-> " + recipe.getName());
        RemoteViews rv = getWidgetRemoteView(context, appWidgetId);
        rv.setTextViewText(R.id.widget_tvrecipename, mRecipe.getName());

        Intent intent = new Intent(context, RecipeListActivity.class );
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        rv.setOnClickPendingIntent(R.id.widget_tvrecipename, pendingIntent);

        //updateAppWidget(context, appWidgetManager, appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_grid_view);


    }

    //--------------------------------------------------------------------------------------------------------------------------------------------

    public static void updateGridWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe recipe) {
        mRecipe = recipe;


        for (int appWidgetId : appWidgetIds) {
            updateAppWidget3(context, appWidgetManager, appWidgetId, recipe);
        }

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);

    }
    //--------------------------------------------------------------------------------------------------------------------------------------------


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------

    private static RemoteViews getWidgetRemoteView(Context context,  int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_container_layout);

        Intent intent = new Intent(context, GridWidgetService.class);

//        // budle-ban atadva mukodik
        //Bundle arg = new Bundle();
        //arg.putParcelable("recipe", recipe);
        //arg.putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        //intent.putExtra("recipe", arg);
        //intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //intent.putExtra("recipe", recipe);

        views.setRemoteAdapter(R.id.widget_grid_view, intent);


        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);

        return views;
    }
    //--------------------------------------------------------------------------------------------------------------------------------------------

}

