interface componentClassIndex {
  id: number;
  parentId: number;
  name: string;
  nameSpace?: string;
}
interface componentClassDetail extends componentClassIndex {
  desc: string;
}
export type { componentClassIndex, componentClassDetail };
