package com.sccl.attech.common.utils;

public abstract interface ITreeNode
{
  public abstract String getParentId();

  public abstract String getMyId();

  public abstract void setDepth(int paramInt);

  public abstract int getDepth();

  public abstract String getShowName();

  public abstract void setLeaf(boolean paramBoolean);

  public abstract boolean isLeaf();
}