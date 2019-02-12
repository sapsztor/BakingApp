package com.dwbi.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dwbi.bakingapp.model.Ingredient;
import com.dwbi.bakingapp.model.Recipe;
import com.dwbi.bakingapp.R;

import java.util.List;


// https://stackoverflow.com/questions/13363046/passing-custom-parcelable-object-extra-or-in-arraylist-to-remoteviewsservice-bre
// https://stackoverflow.com/questions/15243798/advantages-of-using-bundle-instead-of-direct-intent-putextra-in-android

public class GridWidgetService extends RemoteViewsService {
    public static String TAG = "PSX";

    Recipe mRecipe;
    List<Ingredient> mIngredList ;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {


        //Bundle arg = intent.getBundleExtra("recipe");
        //int appWidgetId = arg.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        mRecipe = BakingAppWidgetProvider.mRecipe;
        if (mRecipe == null ){
        }


        if(mRecipe != null){
            mIngredList = mRecipe.getIngredients();
        } else {
            stopSelf();
        }



        for(Ingredient i : mIngredList){
            break;
        }

        return new GridRemoteViewsFactory(this.getApplicationContext(), mRecipe);
    }


}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    public static String TAG = "PSX";

    Context mContext;
    Recipe mRecipe;
    List<Ingredient> mIngredList = null ;


    public GridRemoteViewsFactory(Context applicationContext, Recipe recipe) {
        mContext = applicationContext;
        mRecipe = recipe;



        if(mRecipe != null) {
            mIngredList = mRecipe.getIngredients();
            //Log.d(TAG, "GridWidgetService.GridRemoteViewsFactory constructor mRecipe ok  mIngredList->  "  );
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "GridWidgetService.GridRemoteViewsFactory.onCreate" );
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "GridWidgetService.GridRemoteViewsFactory.onDataSetChanged");
        mRecipe = BakingAppWidgetProvider.mRecipe;
        mIngredList = mRecipe.getIngredients();

    }

    @Override
    public void onDestroy() {
        mRecipe = null;
        mIngredList = null;
    }

    @Override
    public int getCount() {
        if (mIngredList != null) {
            return mIngredList.size();
        } else {
            return 0;
        }

    }


    @Override
    public RemoteViews getViewAt(int position) {

        String ingredient = mIngredList.get(position).getIngredient();
        String quantity = mIngredList.get(position).getQuantity();
        String measure = mIngredList.get(position).getMeasure();

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_item_layout);
        views.setTextViewText(R.id.tvWidgetIngred, ingredient + " " + quantity + " " + measure);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}




