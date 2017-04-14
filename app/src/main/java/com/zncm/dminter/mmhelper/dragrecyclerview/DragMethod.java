package com.zncm.dminter.mmhelper.dragrecyclerview;

public interface DragMethod {
    public void onMove(int fromPosition, int toPosition);
    public void onSwiped(int position);
}
