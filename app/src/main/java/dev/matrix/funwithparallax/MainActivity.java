package dev.matrix.funwithparallax;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * @author rostyslav.lesovyi
 */
public class MainActivity extends ListActivity {

	private final int[] sImages = {
			R.drawable.image1,
			R.drawable.image2,
			R.drawable.image3,
			R.drawable.image4
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new Adapter());

		getListView().setDividerHeight((int) (10 * getResources().getDisplayMetrics().density));
	}

	class Adapter extends BaseAdapter {
		@Override
		public int getCount() {
			return 100;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			int resId = sImages[position % sImages.length];

			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.v_item, parent, false);
				convertView.setTag(convertView.findViewById(R.id.image));
			}

			ImageView imageView = (ImageView) convertView.getTag();
			imageView.setImageResource(resId);

			return convertView;
		}
	}
}
