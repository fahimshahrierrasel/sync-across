export enum ItemType {
  BOOKMARK = "bookmark",
  MULTIMEDIA = "multimedia",
  Note = "note",
}

export function itemTypes(): string[] {
  return Object.values(ItemType);
}
