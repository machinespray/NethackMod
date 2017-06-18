package com.machinespray.ROYAL;

import com.machinespray.ROYAL.items.rings.ItemRing;

import java.util.HashMap;
import java.util.Random;

public class Values {
    //RandomInstance
    public static final Random random = new Random();
    public static ItemRing ringInstance = new ItemRing();
    //TypeEnumValues
    static int sid = 0;
    static HashMap<Integer, EnumDataType> types = new HashMap<Integer, EnumDataType>();
}
