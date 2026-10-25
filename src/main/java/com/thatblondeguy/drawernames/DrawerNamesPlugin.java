package com.thatblondeguy.drawernames;

import com.buuz135.functionalstorage.block.Drawer;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class DrawerNamesPlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(DrawerNameProvider.INSTANCE, Drawer.class);
    }
}
