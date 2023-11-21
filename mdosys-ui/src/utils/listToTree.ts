/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data: any, id: any, parentId: any, children: any) {
  let config = {
    id: id || "id",
    parentId: parentId || "parentId",
    childrenList: children || "children",
  };

  var childrenListMap = {};
  var nodeIds = {};
  var tree = [];

  for (let d of data) {
    let parentId = d[config.parentId];
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = [];
    }
    nodeIds[d[config.id]] = d;
    childrenListMap[parentId].push(d);
  }

  for (let d of data) {
    let parentId = d[config.parentId];
    if (nodeIds[parentId] == null) {
      tree.push(d);
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(o: any) {
    if (childrenListMap[o[config.id]] != null) {
      o[config.childrenList] = childrenListMap[o[config.id]];
    } else {
      o[config.childrenList] = [];
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c);
      }
    }
  }
  return tree;
}

export function addTreeLeafs(menuTree: any, data: any) {
  var tree = [];
  tree.push(...menuTree);
  var childrenListMap = {};
  for (let d of data) {
    let classId = d["classId"];
    if (childrenListMap[classId] == null) {
      childrenListMap[d["classId"]] = [];
    }
    childrenListMap[classId].push(d);
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(tree: any) {
    if (tree["children"]) {
      for (let c of tree["children"]) {
        adaptToChildrenList(c);
      }
    }
    if (tree["children"] == null) {
      tree["children"] = [];
    }
    if (childrenListMap[tree["id"]] != null) {
      var arr: any[] = childrenListMap[tree["id"]];
      for (let item of arr) {
        tree["children"].push(item);
      }
    }
  }
  return tree;
}
