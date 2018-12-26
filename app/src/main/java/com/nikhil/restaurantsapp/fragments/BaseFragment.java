package com.nikhil.restaurantsapp.fragments;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment
{
    ProgressDialog prgDialog;

    public void showPrg(final String message)
    {
        if(getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (prgDialog != null && prgDialog.isShowing())
                        prgDialog.hide();
                    prgDialog = new ProgressDialog(getContext());
                    prgDialog.setMessage(message);
                    prgDialog.show();
                }
            });
        }

    }

    public void hidePrg()
    {
        if(getActivity() != null)
        {
            getActivity().runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (prgDialog != null && prgDialog.isShowing())
                    {
                        prgDialog.dismiss();

                    }
                }
            });
        }
    }
}
