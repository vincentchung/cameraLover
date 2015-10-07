
package com.pictelligent.s40.camera5in1.JPEGEncoder;

// source: typedef struct jpeg_component_info in jpeglib.h

/* Basic info about one component (color channel). */
public class JpegComponentInfo {
    /* These values are fixed over the whole image. */
    /* For compression, they must be supplied by parameter setup; */
    /* for decompression, they are read from the SOF marker. */

    public int component_id; /* identifier for this component (0..255) */// TODO:
                                                                         // short?
    public int component_index; /* its index in SOF or cinfo->comp_info[] */// TODO:
                                                                            // short?
    int h_samp_factor; /* horizontal sampling factor (1..4) */// TODO: short?
    int v_samp_factor; /* vertical sampling factor (1..4) */// TODO: short?
    int quant_tbl_no; /* quantization table selector (0..3) */// TODO: short?

    /* These values may vary between scans. */
    /* For compression, they must be supplied by parameter setup; */
    /* for decompression, they are read from the SOS marker. */
    /* The decompressor output side may not use these variables. */
    int dc_tbl_no; /* DC entropy table selector (0..3) */// TODO: short?
    int ac_tbl_no; /* AC entropy table selector (0..3) */// TODO: short?

    /* Remaining fields should be treated as private by applications. */

    /* These values are computed during compression or decompression startup: */
    /*
     * Component's size in DCT blocks. Any dummy blocks added to complete an MCU
     * are not counted; therefore these values do not depend on whether a scan
     * is interleaved or not.
     */
    int width_in_blocks; // true_comp_width
    int height_in_blocks; // true_comp_height

    /*
     * Size of a DCT block in samples, reflecting any scaling we choose to apply
     * during the DCT step. Values from 1 to 16 are supported. Note that
     * different components may receive different DCT scalings.
     */
    // int DCT_h_scaled_size;
    // int DCT_v_scaled_size;

    // the above are the logical dimensions of the downsampled image
    // These values are computed before starting a scan of the component
    int downsampled_width; /* actual width in samples */
    int downsampled_height; /* actual width in samples */

    // boolean component_needed; /* do we need the value of this component? */

    /* These values are computed before starting a scan of the component. */
    /* The decompressor output side may not use these variables. */
    // int MCU_width; /* number of blocks per MCU, horizontally */
    // int MCU_height; /* number of blocks per MCU, vertically */
    // int MCU_blocks; /* MCU_width * MCU_height */
    // int MCU_sample_width; /* MCU width in samples: MCU_width *
    // DCT_h_scaled_size */
    // int last_col_width; /* # of non-dummy blocks across in last MCU */
    // int last_row_height; /* # of non-dummy blocks down in last MCU */

    /*
     * Saved quantization table for component; NULL if none yet saved. See
     * jdinput.c comments about the need for this information. This field is
     * currently used only for decompression.
     */
    // JQUANT_TBL * quant_table;

    /* Private per-component storage for DCT or IDCT subsystem. */
    // void * dct_table;

}
