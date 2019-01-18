package com.mojodigi.selfiepro.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.mojodigi.selfiepro.R;
import com.mojodigi.selfiepro.adapter.ColorPickerAdapter;
import com.mojodigi.selfiepro.interfaces.Properties;

public class PropertiesFragment extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener {
    private View mView ;
    private Properties mProperties;
    private ColorPickerAdapter mColorPickerAdapter ;


    public PropertiesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_bottom_properties_dialog, container, false);
        return mView ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOnViewCreated(view);
    }

    private void mOnViewCreated(View view) {

        RecyclerView rvColor = view.findViewById(R.id.rvColors);
        SeekBar sbOpacity = view.findViewById(R.id.sbOpacity);
        SeekBar sbBrushSize = view.findViewById(R.id.sbSize);

        sbOpacity.setOnSeekBarChangeListener(this);
        sbBrushSize.setOnSeekBarChangeListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rvColor.setLayoutManager(layoutManager);
        rvColor.setHasFixedSize(true);


        mColorPickerAdapter = new ColorPickerAdapter(getActivity());
        mColorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                if (mProperties != null) {
                    dismiss();
                    mProperties.onColorChanged(colorCode);
                }
            }
        });
        rvColor.setAdapter(mColorPickerAdapter);

    }


    /***********************************************************************************/

    public void setPropertiesChangeListener(Properties properties) {
        mProperties = properties;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.sbOpacity:
                if (mProperties != null) {
                    mProperties.onOpacityChanged(i);
                }
                break;
            case R.id.sbSize:
                if (mProperties != null) {
                    mProperties.onBrushSizeChanged(i);
                }
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}