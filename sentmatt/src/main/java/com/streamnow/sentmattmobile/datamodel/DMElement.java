package com.streamnow.sentmattmobile.datamodel;

import com.streamnow.sentmattmobile.interfaces.IMenuPrintable;

/**
 * !
 * Created by Miguel Estévez on 12/2/16.
 */
public abstract class DMElement implements IMenuPrintable {
    public enum DMElementType {
        DMElementTypeCategory,
        DMElementTypeDocument
    }

    public DMElementType elementType;
}
