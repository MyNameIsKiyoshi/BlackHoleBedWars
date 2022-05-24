/*
 * BlackHoleBedWars
 * Copyright (c) 2022. YumaHisai
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.yumahisai.blholebw.arena;

import java.text.SimpleDateFormat;

public class ArenaManager {

    private int gid = 0;
    private String day = "", month = "";


    public String generateGameID() {
        SimpleDateFormat y = new SimpleDateFormat("yy"), m = new SimpleDateFormat("MM"), d = new SimpleDateFormat("dd");
        String m2 = m.format(System.currentTimeMillis()), d2 = d.format(System.currentTimeMillis());
        if (!(m2.equals(this.month) || d2.equalsIgnoreCase(this.day))) {
            this.month = m2;
            this.day = d2;
            this.gid = 0;
        }
        return "bw_temp_y" + y.format(System.currentTimeMillis()) + "m" + this.month + "d" + this.day + "g" + gid++;
    }

}
