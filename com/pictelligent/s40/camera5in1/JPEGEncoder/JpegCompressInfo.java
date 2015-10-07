
package com.pictelligent.s40.camera5in1.JPEGEncoder;

import java.io.DataOutputStream;

//import com.pictelligent.s40.camera5in1.JPG.JpegComponentInfo;

// source: struct jpeg_compress_struct in jpeglib.h
// keeping variable names unchanged for better readability

// This "structure" contains variables for the JPEG file being encoded.
// Variables specific to a single Y/Cb/Cr component are stored in JpegComponentInfo class.

public class JpegCompressInfo {

    final short NUM_QUANT_TBLS = 4; // quantization tables are numbered 0..3
    final short NUM_HUFF_TBLS = 4; // Huffman tables are numbered 0..3
    final short NUM_ARITH_TBLS = 16; // arith-coding tables are numbered 0..15
    final short MAX_COMPS_IN_SCAN = 4; // JPEG limit on # of components in one
                                       // scan
    final short MAX_SAMP_FACTOR = 4; // JPEG limit on sampling factors
    final short MAX_BLOCKS_IN_MCU = 10; // JPEG limit on # of blocks in an MCU
    final short DCTSIZE = 8; // dct size

    // RGB input data
    public int[] rawData;
    public short[] rawData16bits;

    /*
     * Description of source image --- these fields must be filled in by outer
     * application before starting compression. in_color_space must be correct
     * before you can even call jpeg_set_defaults().
     */

    public int image_width; // input image width
    public int image_height; // input image height

    // public short input_components; /* # of color components in input image */
    // public short in_color_space; /* colorspace of input image */

    // double input_gamma; /* image gamma of input image */

    public short quality; // JPEG compression factor

    /*
     * Compression parameters --- these fields must be set before calling
     * jpeg_start_compress(). We recommend calling jpeg_set_defaults() to
     * initialize everything to reasonable defaults, then changing anything the
     * application specifically wants to change. That way you won't get burnt
     * when new parameters are added. Also note that there are several helper
     * routines to simplify changing parameters.
     */

    // int jpeg_width; /* scaled JPEG image width */
    // int jpeg_height; /* scaled JPEG image height */

    /*
     * Dimensions of actual JPEG image that will be written to file, derived
     * from input dimensions by scaling factors above. These fields are computed
     * by jpeg_start_compress(). You can also use jpeg_calc_jpeg_dimensions() to
     * determine these values in advance of calling jpeg_start_compress().
     */

    public short data_precision; /* bits of precision in image data */

    public short num_components; /* # of color components in JPEG image */

    // public short jpeg_color_space; /* colorspace of JPEG image */

    public JpegComponentInfo comp_info[];
    /* comp_info[i] describes component that appears i'th in SOF */

    // UINT8 arith_dc_L[NUM_ARITH_TBLS]; /* L values for DC arith-coding tables
    // */
    // UINT8 arith_dc_U[NUM_ARITH_TBLS]; /* U values for DC arith-coding tables
    // */
    // UINT8 arith_ac_K[NUM_ARITH_TBLS]; /* Kx values for AC arith-coding tables
    // */

    // int num_scans; /* # of entries in scan_info array */
    // const jpeg_scan_info * scan_info; /* script for multi-scan file, or NULL
    // */
    /*
     * The default value of scan_info is NULL, which causes a single-scan
     * sequential JPEG file to be emitted. To create a multi-scan file, set
     * num_scans and scan_info to point to an array of scan definitions.
     */

    /*
     * The restart interval can be specified in absolute MCUs by setting
     * restart_interval, or in MCU rows by setting restart_in_rows (in which
     * case the correct restart_interval will be figured for each scan).
     */
    // unsigned int restart_interval; /* MCUs per restart, or 0 for no restart
    // */
    // int restart_in_rows; /* if > 0, MCU rows per restart interval */

    /* Parameters controlling emission of special markers. */

    // boolean write_JFIF_header; /* should a JFIF marker be written? */
    // UINT8 JFIF_major_version; /* What to write for the JFIF version number */
    // UINT8 JFIF_minor_version;
    /* These three values are not used by the JPEG code, merely copied */
    /* into the JFIF APP0 marker. density_unit can be 0 for unknown, */
    /* 1 for dots/inch, or 2 for dots/cm. Note that the pixel aspect */
    /* ratio is defined by X_density/Y_density even when density_unit=0. */
    // UINT8 density_unit; /* JFIF code for pixel size units */
    // UINT16 X_density; /* Horizontal pixel density */
    // UINT16 Y_density; /* Vertical pixel density */
    // boolean write_Adobe_marker; /* should an Adobe marker be written? */

    /*
     * State variable: index of next scanline to be written to
     * jpeg_write_scanlines(). Application may use this to control its
     * processing loop, e.g., "while (next_scanline < image_height)".
     */

    // JDIMENSION next_scanline; /* 0 .. image_height-1 */

    /*
     * Remaining fields are known throughout compressor, but generally should
     * not be touched by a surrounding application.
     */

    /*
     * These fields are computed during compression startup
     */
    // boolean progressive_mode; /* TRUE if scan script uses progressive mode */
    // // TODO> same as interleave?
    public short max_h_samp_factor; /* largest h_samp_factor */
    public short max_v_samp_factor; /* largest v_samp_factor */

    // public boolean interleave; // TRUE=interleaved output, FALSE=not // TODO:
    // same as progressive mode?

    public String comment = "Pictelligent";

    // int min_DCT_h_scaled_size; /* smallest DCT_h_scaled_size of any component
    // */
    // int min_DCT_v_scaled_size; /* smallest DCT_v_scaled_size of any component
    // */

    // JDIMENSION total_iMCU_rows; /* # of iMCU rows to be input to coef ctlr */
    /*
     * The coefficient controller receives data in units of MCU rows as defined
     * for fully interleaved scans (whether the JPEG file is interleaved or
     * not). There are v_samp_factor * DCTSIZE sample rows of each component in
     * an "iMCU" (interleaved MCU) row.
     */

    /*
     * These fields are valid during any one scan. They describe the components
     * and MCUs actually appearing in the scan.
     */
    // public short comps_in_scan; /* # of JPEG components in this scan */
    // public JpegComponentInfo cur_comp_info[] = new
    // JpegComponentInfo[MAX_COMPS_IN_SCAN];
    /* *cur_comp_info[i] describes component that appears i'th in SOS */

    public int MCUs_per_row; /* # of MCUs across the image */
    public int MCU_row_count; /* # of MCU rows in the image */

    public int width_in_cosine_domain; // image size rounded up to next MCU
                                       // boundary
    public int height_in_cosine_domain; // image size rounded up to next MCU
                                        // boundary

    public JpegCompressInfo(final int[] inputData, final int width, final int height,
            final short qualityLevel) {

        rawData = inputData;

        image_width = width;
        image_height = height;

        quality = qualityLevel;

        data_precision = 8;

        num_components = 3; // hardcoded to Y Cb Cr

        max_h_samp_factor = 2; // hardcoded to 4:2:0 sampling
        max_v_samp_factor = 2; // hardcoded to 4:2:0 sampling

        comp_info = new JpegComponentInfo[num_components];

        comp_info[0] = new JpegComponentInfo(); // Y
        comp_info[0].component_index = 0;
        comp_info[0].component_id = 1;
        comp_info[0].h_samp_factor = 2; // hardcoded to 4:2:0 sampling
        comp_info[0].v_samp_factor = 2; // hardcoded to 4:2:0 sampling
        comp_info[0].quant_tbl_no = 0; // use tables 0 for luminance
        comp_info[0].dc_tbl_no = 0;
        comp_info[0].ac_tbl_no = 0;

        comp_info[1] = new JpegComponentInfo(); // Cr
        comp_info[1].component_index = 1;
        comp_info[1].component_id = 2;
        comp_info[1].h_samp_factor = 1; // hardcoded to 4:2:0 sampling
        comp_info[1].v_samp_factor = 1; // hardcoded to 4:2:0 sampling
        comp_info[1].quant_tbl_no = 1;
        comp_info[1].dc_tbl_no = 1;
        comp_info[1].ac_tbl_no = 1;

        comp_info[2] = new JpegComponentInfo(); // Cb
        comp_info[2].component_index = 2;
        comp_info[2].component_id = 3;
        comp_info[2].h_samp_factor = 1; // hardcoded to 4:2:0 sampling
        comp_info[2].v_samp_factor = 1; // hardcoded to 4:2:0 sampling
        comp_info[2].quant_tbl_no = 1;
        comp_info[2].dc_tbl_no = 1;
        comp_info[2].ac_tbl_no = 1;

        // cur_comp_info = new JpegComponentInfo[MAX_COMPS_IN_SCAN];
        // cur_comp_info[0] = comp_info[0];
        // cur_comp_info[1] = comp_info[1];
        // cur_comp_info[2] = comp_info[2];

        MCUs_per_row = (image_width + max_h_samp_factor * DCTSIZE - 1)
                / (max_h_samp_factor * DCTSIZE);

        MCU_row_count = (image_height + max_v_samp_factor * DCTSIZE - 1)
                / (max_v_samp_factor * DCTSIZE);

        width_in_cosine_domain = MCUs_per_row * max_h_samp_factor * DCTSIZE;
        height_in_cosine_domain = MCU_row_count * max_v_samp_factor * DCTSIZE;

        if (JpegEncoder.mDebug) {
            System.out.println("JPEG compression parameters:");
            System.out.println(" * Image size: " + image_width + "x" + image_height
                    + ", MCU rounded size " + width_in_cosine_domain + "x"
                    + height_in_cosine_domain);
            System.out.println(" * MCUs_per_row: " + MCUs_per_row + ", MCU_row_count: "
                    + MCU_row_count);

        }

    }

    public JpegCompressInfo(final short[] inputData, final int width, final int height,
            final short qualityLevel) {

        rawData = null;
        rawData16bits = inputData;

        image_width = width;
        image_height = height;

        quality = qualityLevel;

        data_precision = 8;

        num_components = 3; // hardcoded to Y Cb Cr

        max_h_samp_factor = 2; // hardcoded to 4:2:0 sampling
        max_v_samp_factor = 2; // hardcoded to 4:2:0 sampling

        comp_info = new JpegComponentInfo[num_components];

        comp_info[0] = new JpegComponentInfo(); // Y
        comp_info[0].component_index = 0;
        comp_info[0].component_id = 1;
        comp_info[0].h_samp_factor = 2; // hardcoded to 4:2:0 sampling
        comp_info[0].v_samp_factor = 2; // hardcoded to 4:2:0 sampling
        comp_info[0].quant_tbl_no = 0; // use tables 0 for luminance
        comp_info[0].dc_tbl_no = 0;
        comp_info[0].ac_tbl_no = 0;

        comp_info[1] = new JpegComponentInfo(); // Cr
        comp_info[1].component_index = 1;
        comp_info[1].component_id = 2;
        comp_info[1].h_samp_factor = 1; // hardcoded to 4:2:0 sampling
        comp_info[1].v_samp_factor = 1; // hardcoded to 4:2:0 sampling
        comp_info[1].quant_tbl_no = 1;
        comp_info[1].dc_tbl_no = 1;
        comp_info[1].ac_tbl_no = 1;

        comp_info[2] = new JpegComponentInfo(); // Cb
        comp_info[2].component_index = 2;
        comp_info[2].component_id = 3;
        comp_info[2].h_samp_factor = 1; // hardcoded to 4:2:0 sampling
        comp_info[2].v_samp_factor = 1; // hardcoded to 4:2:0 sampling
        comp_info[2].quant_tbl_no = 1;
        comp_info[2].dc_tbl_no = 1;
        comp_info[2].ac_tbl_no = 1;

        // cur_comp_info = new JpegComponentInfo[MAX_COMPS_IN_SCAN];
        // cur_comp_info[0] = comp_info[0];
        // cur_comp_info[1] = comp_info[1];
        // cur_comp_info[2] = comp_info[2];

        MCUs_per_row = (image_width + max_h_samp_factor * DCTSIZE - 1)
                / (max_h_samp_factor * DCTSIZE);

        MCU_row_count = (image_height + max_v_samp_factor * DCTSIZE - 1)
                / (max_v_samp_factor * DCTSIZE);

        width_in_cosine_domain = MCUs_per_row * max_h_samp_factor * DCTSIZE;
        height_in_cosine_domain = MCU_row_count * max_v_samp_factor * DCTSIZE;

        if (JpegEncoder.mDebug) {
            System.out.println("JPEG compression parameters:");
            System.out.println(" * Image size: " + image_width + "x" + image_height
                    + ", MCU rounded size " + width_in_cosine_domain + "x"
                    + height_in_cosine_domain);
            System.out.println(" * MCUs_per_row: " + MCUs_per_row + ", MCU_row_count: "
                    + MCU_row_count);

        }

    }

    /*
     * public JpegCompressInfo(int quality) { Tables dctTables = new
     * Tables(quality); int len, i; quant_tbl = new
     * short[NUM_QUANT_TBLS][DCTSIZE * DCTSIZE];
     * dctTables.getLuminanceQuantTable(quant_tbl[0]);
     * dctTables.getChrominanceQuantTable(quant_tbl[1]); quant_tbl[2] = null;
     * quant_tbl[3] = null; // coefficient quantization tables, or NULL if not
     * defined dc_huff_tbl = new HuffTbl[NUM_HUFF_TBLS]; dc_huff_tbl[0] = new
     * HuffTbl(dctTables.dc_luminance_bits, dctTables.dc_luminance_val);
     * dc_huff_tbl[1] = new HuffTbl(dctTables.dc_chrominance_bits,
     * dctTables.dc_chrominance_val); dc_huff_tbl[2] = null; dc_huff_tbl[3] =
     * null; ac_huff_tbl = new HuffTbl[NUM_HUFF_TBLS]; ac_huff_tbl[0] = new
     * HuffTbl(dctTables.ac_luminance_bits, dctTables.ac_luminance_val);
     * ac_huff_tbl[1] = new HuffTbl(dctTables.ac_chrominance_bits,
     * dctTables.ac_chrominance_val); ac_huff_tbl[2] = null; ac_huff_tbl[3] =
     * null; // Huffman coding tables, or NULL if not defined }
     */
}
