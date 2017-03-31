/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.marktony.espresso;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.DefaultOffsettingHelper;
import android.support.wearable.view.WearableRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.github.marktony.espresso.data.Package;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Package> list = new ArrayList<>();
        Package p1 = new Package();
        p1.setName("SF");
        list.add(p1);

        Package p2 = new Package();
        p2.setName("YUNDA");
        list.add(p2);

        WearableRecyclerView recyclerView = (WearableRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setCenterEdgeItems(true);

        recyclerView.setOffsettingHelper(new DefaultOffsettingHelper());

        PackageRecyclerAdapter adapter = new PackageRecyclerAdapter(list);
        recyclerView.setAdapter(adapter);

    }
}
