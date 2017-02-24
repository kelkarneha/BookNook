package com.example.android.booknook.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Administrator on 2/12/2017.
 */

public class BookWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BookDataProvider(this, intent);
    }
}
