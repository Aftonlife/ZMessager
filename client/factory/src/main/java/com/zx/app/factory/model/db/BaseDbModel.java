package com.zx.app.factory.model.db;

import com.raizlabs.android.dbflow.structure.BaseModel;
import com.zx.app.factory.utils.DiffUiDataCallback;

/**
 * author Afton
 * date 2020/3/14
 */
public abstract class BaseDbModel<Model> extends BaseModel
        implements DiffUiDataCallback.UiDataDiffer<Model> {
}
