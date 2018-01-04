package com.rosinante.firestorerealtimechat.chatabstract;

/**
 * Created by Rosinante24 on 04/01/18.
 */

public abstract class AbstractChat {

    public abstract String getName();

    public abstract String getMessage();

    public abstract String getUid();

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

}
