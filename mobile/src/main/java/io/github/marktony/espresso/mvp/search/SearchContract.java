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

package io.github.marktony.espresso.mvp.search;

import java.util.List;

import io.github.marktony.espresso.data.Company;
import io.github.marktony.espresso.data.Package;
import io.github.marktony.espresso.mvp.BasePresenter;
import io.github.marktony.espresso.mvp.BaseView;

/**
 * Created by lizhaotailang on 2017/3/26.
 */

public interface SearchContract {

    interface View extends BaseView<Presenter> {

        void showResult(List<Package> packages, List<Company> companies);

    }

    interface Presenter extends BasePresenter {

        void search(String keyWords);

    }

}
