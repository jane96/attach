package com.sccl.attech.common.utils;

import java.util.HashMap;
import java.util.Vector;

public class TreeNodeUtils {
	protected int nTreeDepth = 0;

	protected void scanTreeNode(Vector vNode, HashMap hmNode, ITreeNode pNode)
			throws Exception {
		String parentId = "0";
		if (pNode != null)
			parentId = pNode.getMyId();
		else
			this.nTreeDepth = 0;
		Object objVector = hmNode.get(String.valueOf(parentId));
		if (objVector != null) {
			if (pNode != null)
				pNode.setLeaf(false);
			Vector vChildNodes = (Vector) objVector;
			for (int i = 0; i < vChildNodes.size(); ++i) {
				ITreeNode node = (ITreeNode) vChildNodes.elementAt(i);
				vNode.add(node);
				node.setDepth(this.nTreeDepth);
				this.nTreeDepth += 1;
				scanTreeNode(vNode, hmNode, node);
			}
		} else if (pNode != null) {
			pNode.setLeaf(true);
		}
		this.nTreeDepth -= 1;
	}

	public static void SortNodeToTree(ITreeNode[] nodes) throws Exception {
		HashMap hmNode = new HashMap();
		Vector vParentId = new Vector();
		Vector vNode = new Vector();
		for (int i = 0; i < nodes.length; ++i) {
			Vector vChildNodes;
			String parentId = nodes[i].getParentId();
			if(StringUtils.isBlank(parentId)) parentId = "0";
			Object objVector = hmNode.get(parentId);
			nodes[i].setDepth(-1);
			if (objVector == null) {
				vChildNodes = new Vector();
				vChildNodes.add(nodes[i]);
				hmNode.put(parentId, vChildNodes);
			} else {
				vChildNodes = (Vector) objVector;
				vChildNodes.add(nodes[i]);
			}
		}

		TreeNodeUtils utils = new TreeNodeUtils();
		utils.scanTreeNode(vNode, hmNode, null);

		vNode.copyInto(nodes);
	}

	public static void SortNodeToTree(Object[] objNodes) throws Exception {
		if (objNodes != null) {
			ITreeNode[] nodes = new ITreeNode[objNodes.length];
			for (int i = 0; i < nodes.length; ++i)
				nodes[i] = ((ITreeNode) objNodes[i]);

			SortNodeToTree(nodes);
			for (int i = 0; i < nodes.length; ++i)
				objNodes[i] = nodes[i];
		}
	}
}