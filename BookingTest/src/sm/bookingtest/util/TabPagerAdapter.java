package sm.bookingtest.util;

import sm.bookingtest.mypagepurchase.MyPageFragment;
import sm.bookingtest.purchase.PurchaseFragment;
import sm.bookingtest.register.RegisterFragmentChoice;
import sm.bookingtest.sell.SellFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter {

	public TabPagerAdapter(FragmentManager fm) 
	{
		super(fm);
	}
	
	@Override
	public Fragment getItem(int index) 
	{
	
		switch (index) 
		{
		case 0:
			return new SellFragment();
		
		case 1:
			return new PurchaseFragment();
			
		case 2:
			return new RegisterFragmentChoice();
			
		case 3:
			return new MyPageFragment();
		}
		return null;
	}

	@Override
	public int getCount() 
	{
		return 4;
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}

}
