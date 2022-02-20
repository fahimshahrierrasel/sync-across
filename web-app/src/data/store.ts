import { createContext, Dispatch } from "react";
import { ISyncItem } from "./models/isync-item";
import { ITagItem } from "./models/itag-item";

export interface IAppState {
  syncItems: ISyncItem[];
  tags: ITagItem[];
  isLoading: boolean;
}

export interface IAppAction {
  type: ActionTypes;
  data: any;
}

export enum ActionTypes {
  ToggleLoading,
  AddSyncItems,
  AddSyncItem,
  UpdateSyncItem,
  DeleteSyncItem,
  AddTags,
  AddTag,
}

export const DEFAULT_STATE: IAppState = {
  syncItems: [],
  tags: [],
  isLoading: false,
};

export const AppStoreContext = createContext<{
  state: IAppState;
  dispatch: Dispatch<any>;
}>({
  state: DEFAULT_STATE,
  dispatch: () => {},
});

export function appReducer(state: IAppState, action) {
  switch (action.type) {
    case ActionTypes.ToggleLoading:
      return { ...state, isLoading: action.data };
    case ActionTypes.AddSyncItems:
      return { ...state, syncItems: action.data };
    case ActionTypes.AddSyncItem:
      return { ...state, syncItems: [action.data, ...state.syncItems] };
    case ActionTypes.UpdateSyncItem: {
      const syncItems = state.syncItems.map((syncItem: ISyncItem) => {
        if (syncItem.id === action.data.id) {
          return action.data;
        }
        return syncItem;
      });
      return { ...state, syncItems: syncItems };
    }
    case ActionTypes.DeleteSyncItem: {
      const messages = state.syncItems.filter(
        (message: ISyncItem) => message.id !== action.data
      );
      return { ...state, syncItems: messages };
    }
    case ActionTypes.AddTags:
      return { ...state, tags: action.data };
    case ActionTypes.AddTag:
      return { ...state, tags: [...state.tags, action.data] };
    default:
      throw new Error();
  }
}
