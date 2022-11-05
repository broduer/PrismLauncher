// SPDX-License-Identifier: GPL-3.0-only
/*
 *  Prism Launcher
 *
 *  Copyright (C) 2022 icelimetea <fr3shtea@outlook.com>
 *  Copyright (C) 2022 TheKodeToad <TheKodeToad@proton.me>
 *  Copyright (C) 2022 solonovamax <solonovamax@12oclockpoint.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, version 3.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Linking this library statically or dynamically with other modules is
 *  making a combined work based on this library. Thus, the terms and
 *  conditions of the GNU General Public License cover the whole
 *  combination.
 *
 *  As a special exception, the copyright holders of this library give
 *  you permission to link this library with independent modules to
 *  produce an executable, regardless of the license terms of these
 *  independent modules, and to copy and distribute the resulting
 *  executable under terms of your choice, provided that you also meet,
 *  for each linked independent module, the terms and conditions of the
 *  license of that module. An independent module is a module which is
 *  not derived from or based on this library. If you modify this
 *  library, you may extend this exception to your version of the
 *  library, but you are not obliged to do so. If you do not wish to do
 *  so, delete this exception statement from your version.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * This file incorporates work covered by the following copyright and
 * permission notice:
 *
 *      Copyright 2013-2021 MultiMC Contributors
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

package net.minecraft;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

/**
 * WARNING: This class is reflectively accessed by legacy Forge versions.
 * <p>
 * Changing field and method declarations without further testing is not
 * recommended.
 */
public final class Launcher extends Applet implements AppletStub {

    private static final long serialVersionUID = 1L;

    private final Map<String, String> params = new TreeMap<>();

    private Applet wrappedApplet;
    private final URL documentBase;
    private boolean active = false;

    public Launcher(Applet applet) {
        this(applet, null);
    }

    public Launcher(Applet applet, URL documentBase) {
        setLayout(new BorderLayout());

        this.add(applet, "Center");

        wrappedApplet = applet;

        try {
            if (documentBase != null) {
                this.documentBase = documentBase;
            } else if (applet.getClass().getPackage().getName().startsWith("com.mojang.")) {
                // Special case only for Classic versions

                // TODO: 2022-10-27 Can this be changed to https
                this.documentBase = new URL("http", "www.minecraft.net", 80, "/game/");
            } else {
                // TODO: 2022-10-27 Can this be changed to https?
                this.documentBase = new URL("http://www.minecraft.net/game/");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void replace(Applet applet) {
        wrappedApplet = applet;

        applet.setStub(this);
        applet.setSize(this.getWidth(), this.getHeight());

        setLayout(new BorderLayout());
        this.add(applet, "Center");

        applet.init();

        this.active = true;

        applet.start();

        this.validate();
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public URL getDocumentBase() {
        return this.documentBase;
    }

    @Override
    public URL getCodeBase() {
        try {
            // TODO: 2022-10-27 Can this be changed to https?
            return new URL("http://www.minecraft.net/game/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getParameter(String name) {
        String param = this.params.get(name);

        if (param != null)
            return param;

        try {
            return super.getParameter(name);
        } catch (Exception ignored) {
        }

        return null;
    }

    @Override
    public void resize(int width, int height) {
        this.wrappedApplet.resize(width, height);
    }

    @Override
    public void resize(Dimension size) {
        this.wrappedApplet.resize(size);
    }

    @Override
    public void init() {
        if (this.wrappedApplet != null)
            this.wrappedApplet.init();
    }

    @Override
    public void start() {
        this.wrappedApplet.start();

        this.active = true;
    }

    @Override
    public void stop() {
        this.wrappedApplet.stop();

        this.active = false;
    }

    @Override
    public void destroy() {
        this.wrappedApplet.destroy();
    }

    @Override
    public void appletResize(int width, int height) {
        this.wrappedApplet.resize(width, height);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        this.wrappedApplet.setVisible(visible);
    }

    @Override
    public void paint(Graphics graphics) {
    }

    @Override
    public void update(Graphics graphics) {
    }

    public void setParameter(String name, String value) {
        this.params.put(name, value);
    }

}
