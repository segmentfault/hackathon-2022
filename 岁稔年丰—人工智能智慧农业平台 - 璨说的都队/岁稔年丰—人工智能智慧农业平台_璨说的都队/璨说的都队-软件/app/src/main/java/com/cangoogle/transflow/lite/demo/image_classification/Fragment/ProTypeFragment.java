package com.baidu.paddle.lite.demo.image_classification.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baidu.paddle.lite.demo.image_classification.Adapter.GridViewAdapter;
import com.baidu.paddle.lite.demo.image_classification.Adapter.Scroll_menu_Grid_Adapter;
import com.baidu.paddle.lite.demo.image_classification.Bean.Scroll_menu_Grid_Bean;
import com.baidu.paddle.lite.demo.image_classification.Bean.Type;
import com.baidu.paddle.lite.demo.image_classification.R;
import com.baidu.paddle.lite.demo.image_classification.Utils.Model;
import com.baidu.paddle.lite.demo.image_classification.View.BannerIndicator;
import com.youth.banner.Banner;
import com.youth.banner.transformer.ScaleInTransformer;
import com.youth.banner.util.BannerUtils;

import java.util.ArrayList;

public class ProTypeFragment extends Fragment {

	private ArrayList<Type> list;
	private GridView gridView;
	private GridViewAdapter adapter;
	private Type type;
	private String typename;
	private int icon;
	protected Banner banner;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		banner = getView().findViewById(R.id.banner_pro_type);

		Scroll_menu_Grid_Adapter adapters = new Scroll_menu_Grid_Adapter(Scroll_menu_Grid_Bean.getTestData());

		banner.setAdapter(adapters)//设置适配器
				.setCurrentItem(0,false)
				.addBannerLifecycleObserver(this)//添加生命周期观察者
				.setBannerRound(BannerUtils.dp2px(13))//圆角
				.setLoopTime(3500)
				.addPageTransformer(new ScaleInTransformer())//添加切换效果
				.setIndicator(new BannerIndicator(getContext()));//设置指示器


	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pro_type, null);
		gridView = (GridView) view.findViewById(R.id.listView);
		int index = getArguments().getInt("index");

		typename = Model.toolsList[index];
		icon = Model.iconList[index];

		GetTypeList();
		adapter = new GridViewAdapter(getActivity(), list);
		gridView.setAdapter(adapter);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int postion,
                                    long arg3) {
			}
		});
		return view;
	}

	private void GetTypeList() {
		list = new ArrayList<Type>();
		for (int i = 1; i < 15; i++) {
			type = new Type(i, typename + i, icon);
			list.add(type);
		}
	}
}
