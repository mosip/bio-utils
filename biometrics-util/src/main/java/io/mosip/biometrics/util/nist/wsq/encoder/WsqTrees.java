package io.mosip.biometrics.util.nist.wsq.encoder;

/**
 * Wavelet (W) and quantization (Q) decomposition trees from public-domain NIST
 * NBIS {@code tree.c} ({@code build_w_tree}, {@code build_q_tree},
 * {@code w_tree4}, {@code q_tree16}, {@code q_tree4}).
 * <p>
 * The W-tree describes 20 analysis rectangles used by {@link WsqWavelet}; the
 * Q-tree splits those into 64 (60 live) subband rectangles used by
 * {@link WsqQuantizer}. Odd widths/heights follow NBIS split rules, including
 * the special cases for nodes 4 and 5.
 * </p>
 */
final class WsqTrees {

	/**
	 * One wavelet-tree node: origin, size, and whether the high-pass result is
	 * stored on the left (row) or top (column) of the rectangle.
	 */
	static final class WTree {
		/** Horizontal origin of this node inside the image. */
		int x;
		/** Vertical origin of this node inside the image. */
		int y;
		/** Node width in pixels. */
		int lenx;
		/** Node height in pixels. */
		int leny;
		/** Non-zero when the row (horizontal) split stores high-pass first. */
		int invRw;
		/** Non-zero when the column (vertical) split stores high-pass first. */
		int invCl;
	}

	/**
	 * One quantization-tree node locating a coded subband inside the wavelet
	 * image.
	 */
	static final class QTree {
		/** Horizontal origin of the subband. */
		int x;
		/** Vertical origin of the subband. */
		int y;
		/** Subband width in pixels. */
		int lenx;
		/** Subband height in pixels. */
		int leny;
	}

	/** Utility holder; not instantiated. */
	private WsqTrees() {
	}

	/**
	 * Builds both trees for an image of {@code width × height}.
	 *
	 * @param wTree destination array of length {@link WsqConstants#W_TREELEN}
	 * @param qTree destination array of length {@link WsqConstants#Q_TREELEN}
	 * @param width image width
	 * @param height image height
	 */
	static void build(WTree[] wTree, QTree[] qTree, int width, int height) {
		buildWTree(wTree, width, height);
		buildQTree(wTree, qTree);
	}

	/**
	 * Fills the 20-node wavelet tree, including inverse-split flags and the
	 * deepest LL node (index 19).
	 *
	 * @param wTree  destination nodes
	 * @param width  image width
	 * @param height image height
	 */
	static void buildWTree(WTree[] wTree, int width, int height) {
		for (int node = 0; node < 20; node++) {
			wTree[node] = new WTree();
		}
		wTree[2].invRw = 1;
		wTree[4].invRw = 1;
		wTree[7].invRw = 1;
		wTree[9].invRw = 1;
		wTree[11].invRw = 1;
		wTree[13].invRw = 1;
		wTree[16].invRw = 1;
		wTree[18].invRw = 1;
		wTree[3].invCl = 1;
		wTree[5].invCl = 1;
		wTree[8].invCl = 1;
		wTree[9].invCl = 1;
		wTree[12].invCl = 1;
		wTree[13].invCl = 1;
		wTree[17].invCl = 1;
		wTree[18].invCl = 1;

		wTree4(wTree, 0, 1, width, height, 0, 0, 1);

		int lenx;
		int lenx2;
		int leny;
		int leny2;
		if ((wTree[1].lenx % 2) == 0) {
			lenx = wTree[1].lenx / 2;
			lenx2 = lenx;
		} else {
			lenx = (wTree[1].lenx + 1) / 2;
			lenx2 = lenx - 1;
		}
		if ((wTree[1].leny % 2) == 0) {
			leny = wTree[1].leny / 2;
			leny2 = leny;
		} else {
			leny = (wTree[1].leny + 1) / 2;
			leny2 = leny - 1;
		}

		wTree4(wTree, 4, 6, lenx2, leny, lenx, 0, 0);
		wTree4(wTree, 5, 10, lenx, leny2, 0, leny, 0);
		wTree4(wTree, 14, 15, lenx, leny, 0, 0, 0);

		wTree[19].x = 0;
		wTree[19].y = 0;
		if ((wTree[15].lenx % 2) == 0) {
			wTree[19].lenx = wTree[15].lenx / 2;
		} else {
			wTree[19].lenx = (wTree[15].lenx + 1) / 2;
		}
		if ((wTree[15].leny % 2) == 0) {
			wTree[19].leny = wTree[15].leny / 2;
		} else {
			wTree[19].leny = (wTree[15].leny + 1) / 2;
		}
	}

	/**
	 * Splits one parent rectangle into four child W-tree nodes (NBIS
	 * {@code w_tree4}).
	 *
	 * @param wTree  tree being filled
	 * @param start1 parent node index
	 * @param start2 first child node index
	 * @param lenx   parent width
	 * @param leny   parent height
	 * @param x      parent origin x
	 * @param y      parent origin y
	 * @param stop1  non-zero to skip populating the fourth child geometry (root)
	 */
	private static void wTree4(WTree[] wTree, int start1, int start2, int lenx, int leny, int x, int y, int stop1) {
		int p1 = start1;
		int p2 = start2;
		int evenx = lenx % 2;
		int eveny = leny % 2;

		wTree[p1].x = x;
		wTree[p1].y = y;
		wTree[p1].lenx = lenx;
		wTree[p1].leny = leny;

		wTree[p2].x = x;
		wTree[p2 + 2].x = x;
		wTree[p2].y = y;
		wTree[p2 + 1].y = y;

		if (evenx == 0) {
			wTree[p2].lenx = lenx / 2;
			wTree[p2 + 1].lenx = wTree[p2].lenx;
		} else if (p1 == 4) {
			wTree[p2].lenx = (lenx - 1) / 2;
			wTree[p2 + 1].lenx = wTree[p2].lenx + 1;
		} else {
			wTree[p2].lenx = (lenx + 1) / 2;
			wTree[p2 + 1].lenx = wTree[p2].lenx - 1;
		}
		wTree[p2 + 1].x = wTree[p2].lenx + x;
		if (stop1 == 0) {
			wTree[p2 + 3].lenx = wTree[p2 + 1].lenx;
			wTree[p2 + 3].x = wTree[p2 + 1].x;
		}
		wTree[p2 + 2].lenx = wTree[p2].lenx;

		if (eveny == 0) {
			wTree[p2].leny = leny / 2;
			wTree[p2 + 2].leny = wTree[p2].leny;
		} else if (p1 == 5) {
			wTree[p2].leny = (leny - 1) / 2;
			wTree[p2 + 2].leny = wTree[p2].leny + 1;
		} else {
			wTree[p2].leny = (leny + 1) / 2;
			wTree[p2 + 2].leny = wTree[p2].leny - 1;
		}
		wTree[p2 + 2].y = wTree[p2].leny + y;
		if (stop1 == 0) {
			wTree[p2 + 3].leny = wTree[p2 + 2].leny;
			wTree[p2 + 3].y = wTree[p2 + 2].y;
		}
		wTree[p2 + 1].leny = wTree[p2].leny;
	}

	/**
	 * Fills the 64-node quantization tree from the wavelet tree (NBIS
	 * {@code build_q_tree}).
	 *
	 * @param wTree source wavelet tree
	 * @param qTree destination quantization tree
	 */
	static void buildQTree(WTree[] wTree, QTree[] qTree) {
		for (int i = 0; i < qTree.length; i++) {
			qTree[i] = new QTree();
		}
		qTree16(qTree, 3, wTree[14].lenx, wTree[14].leny, wTree[14].x, wTree[14].y, 0, 0);
		qTree16(qTree, 19, wTree[4].lenx, wTree[4].leny, wTree[4].x, wTree[4].y, 0, 1);
		qTree16(qTree, 48, wTree[0].lenx, wTree[0].leny, wTree[0].x, wTree[0].y, 0, 0);
		qTree16(qTree, 35, wTree[5].lenx, wTree[5].leny, wTree[5].x, wTree[5].y, 1, 0);
		qTree4(qTree, 0, wTree[19].lenx, wTree[19].leny, wTree[19].x, wTree[19].y);
	}

	/**
	 * Splits a rectangle into 16 Q-tree nodes (NBIS {@code q_tree16}).
	 *
	 * @param qTree destination
	 * @param start first node index
	 * @param lenx  parent width
	 * @param leny  parent height
	 * @param x     parent origin x
	 * @param y     parent origin y
	 * @param rw    row-split bias for odd heights
	 * @param cl    column-split bias for odd widths
	 */
	private static void qTree16(QTree[] qTree, int start, int lenx, int leny, int x, int y, int rw, int cl) {
		int p = start;
		int evenx = lenx % 2;
		int eveny = leny % 2;
		int tempx;
		int temp2x;
		int tempy;
		int temp2y;

		if (evenx == 0) {
			tempx = lenx / 2;
			temp2x = tempx;
		} else if (cl != 0) {
			temp2x = (lenx + 1) / 2;
			tempx = temp2x - 1;
		} else {
			tempx = (lenx + 1) / 2;
			temp2x = tempx - 1;
		}

		if (eveny == 0) {
			tempy = leny / 2;
			temp2y = tempy;
		} else if (rw != 0) {
			temp2y = (leny + 1) / 2;
			tempy = temp2y - 1;
		} else {
			tempy = (leny + 1) / 2;
			temp2y = tempy - 1;
		}

		evenx = tempx % 2;
		eveny = tempy % 2;

		qTree[p].x = x;
		qTree[p + 2].x = x;
		qTree[p].y = y;
		qTree[p + 1].y = y;
		if (evenx == 0) {
			qTree[p].lenx = tempx / 2;
			qTree[p + 1].lenx = qTree[p].lenx;
			qTree[p + 2].lenx = qTree[p].lenx;
			qTree[p + 3].lenx = qTree[p].lenx;
		} else {
			qTree[p].lenx = (tempx + 1) / 2;
			qTree[p + 1].lenx = qTree[p].lenx - 1;
			qTree[p + 2].lenx = qTree[p].lenx;
			qTree[p + 3].lenx = qTree[p + 1].lenx;
		}
		qTree[p + 1].x = x + qTree[p].lenx;
		qTree[p + 3].x = qTree[p + 1].x;
		if (eveny == 0) {
			qTree[p].leny = tempy / 2;
			qTree[p + 1].leny = qTree[p].leny;
			qTree[p + 2].leny = qTree[p].leny;
			qTree[p + 3].leny = qTree[p].leny;
		} else {
			qTree[p].leny = (tempy + 1) / 2;
			qTree[p + 1].leny = qTree[p].leny;
			qTree[p + 2].leny = qTree[p].leny - 1;
			qTree[p + 3].leny = qTree[p + 2].leny;
		}
		qTree[p + 2].y = y + qTree[p].leny;
		qTree[p + 3].y = qTree[p + 2].y;

		evenx = temp2x % 2;
		qTree[p + 4].x = x + tempx;
		qTree[p + 6].x = qTree[p + 4].x;
		qTree[p + 4].y = y;
		qTree[p + 5].y = y;
		qTree[p + 6].y = qTree[p + 2].y;
		qTree[p + 7].y = qTree[p + 2].y;
		qTree[p + 4].leny = qTree[p].leny;
		qTree[p + 5].leny = qTree[p].leny;
		qTree[p + 6].leny = qTree[p + 2].leny;
		qTree[p + 7].leny = qTree[p + 2].leny;
		if (evenx == 0) {
			qTree[p + 4].lenx = temp2x / 2;
			qTree[p + 5].lenx = qTree[p + 4].lenx;
			qTree[p + 6].lenx = qTree[p + 4].lenx;
			qTree[p + 7].lenx = qTree[p + 4].lenx;
		} else {
			qTree[p + 5].lenx = (temp2x + 1) / 2;
			qTree[p + 4].lenx = qTree[p + 5].lenx - 1;
			qTree[p + 6].lenx = qTree[p + 4].lenx;
			qTree[p + 7].lenx = qTree[p + 5].lenx;
		}
		qTree[p + 5].x = qTree[p + 4].x + qTree[p + 4].lenx;
		qTree[p + 7].x = qTree[p + 5].x;

		eveny = temp2y % 2;
		qTree[p + 8].x = x;
		qTree[p + 9].x = qTree[p + 1].x;
		qTree[p + 10].x = x;
		qTree[p + 11].x = qTree[p + 1].x;
		qTree[p + 8].y = y + tempy;
		qTree[p + 9].y = qTree[p + 8].y;
		qTree[p + 8].lenx = qTree[p].lenx;
		qTree[p + 9].lenx = qTree[p + 1].lenx;
		qTree[p + 10].lenx = qTree[p].lenx;
		qTree[p + 11].lenx = qTree[p + 1].lenx;
		if (eveny == 0) {
			qTree[p + 8].leny = temp2y / 2;
			qTree[p + 9].leny = qTree[p + 8].leny;
			qTree[p + 10].leny = qTree[p + 8].leny;
			qTree[p + 11].leny = qTree[p + 8].leny;
		} else {
			qTree[p + 10].leny = (temp2y + 1) / 2;
			qTree[p + 11].leny = qTree[p + 10].leny;
			qTree[p + 8].leny = qTree[p + 10].leny - 1;
			qTree[p + 9].leny = qTree[p + 8].leny;
		}
		qTree[p + 10].y = qTree[p + 8].y + qTree[p + 8].leny;
		qTree[p + 11].y = qTree[p + 10].y;

		qTree[p + 12].x = qTree[p + 4].x;
		qTree[p + 13].x = qTree[p + 5].x;
		qTree[p + 14].x = qTree[p + 4].x;
		qTree[p + 15].x = qTree[p + 5].x;
		qTree[p + 12].y = qTree[p + 8].y;
		qTree[p + 13].y = qTree[p + 8].y;
		qTree[p + 14].y = qTree[p + 10].y;
		qTree[p + 15].y = qTree[p + 10].y;
		qTree[p + 12].lenx = qTree[p + 4].lenx;
		qTree[p + 13].lenx = qTree[p + 5].lenx;
		qTree[p + 14].lenx = qTree[p + 4].lenx;
		qTree[p + 15].lenx = qTree[p + 5].lenx;
		qTree[p + 12].leny = qTree[p + 8].leny;
		qTree[p + 13].leny = qTree[p + 8].leny;
		qTree[p + 14].leny = qTree[p + 10].leny;
		qTree[p + 15].leny = qTree[p + 10].leny;
	}

	/**
	 * Splits the deepest LL rectangle into four Q-tree nodes (NBIS
	 * {@code q_tree4}).
	 *
	 * @param qTree destination
	 * @param start first node index (always 0)
	 * @param lenx  parent width
	 * @param leny  parent height
	 * @param x     parent origin x
	 * @param y     parent origin y
	 */
	private static void qTree4(QTree[] qTree, int start, int lenx, int leny, int x, int y) {
		int p = start;
		int evenx = lenx % 2;
		int eveny = leny % 2;
		qTree[p].x = x;
		qTree[p + 2].x = x;
		qTree[p].y = y;
		qTree[p + 1].y = y;
		if (evenx == 0) {
			qTree[p].lenx = lenx / 2;
			qTree[p + 1].lenx = qTree[p].lenx;
			qTree[p + 2].lenx = qTree[p].lenx;
			qTree[p + 3].lenx = qTree[p].lenx;
		} else {
			qTree[p].lenx = (lenx + 1) / 2;
			qTree[p + 1].lenx = qTree[p].lenx - 1;
			qTree[p + 2].lenx = qTree[p].lenx;
			qTree[p + 3].lenx = qTree[p + 1].lenx;
		}
		qTree[p + 1].x = x + qTree[p].lenx;
		qTree[p + 3].x = qTree[p + 1].x;
		if (eveny == 0) {
			qTree[p].leny = leny / 2;
			qTree[p + 1].leny = qTree[p].leny;
			qTree[p + 2].leny = qTree[p].leny;
			qTree[p + 3].leny = qTree[p].leny;
		} else {
			qTree[p].leny = (leny + 1) / 2;
			qTree[p + 1].leny = qTree[p].leny;
			qTree[p + 2].leny = qTree[p].leny - 1;
			qTree[p + 3].leny = qTree[p + 2].leny;
		}
		qTree[p + 2].y = y + qTree[p].leny;
		qTree[p + 3].y = qTree[p + 2].y;
	}
}
