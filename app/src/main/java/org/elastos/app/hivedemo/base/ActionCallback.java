package org.elastos.app.hivedemo.base;

import org.elastos.app.hivedemo.action.ActionType;

public interface ActionCallback<T> {
    void onPreAction(ActionType type);
    void onFail(ActionType type , Exception e);
    void onSuccess(ActionType type , T body);
}
