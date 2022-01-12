package gss.workshop.testing.tests;

import gss.workshop.testing.pojo.board.BoardCreationRes;
import gss.workshop.testing.pojo.card.CardCreationRes;
import gss.workshop.testing.pojo.card.CardUpdateRes;
import gss.workshop.testing.pojo.list.ListCreationRes;
import gss.workshop.testing.requests.RequestFactory;
import gss.workshop.testing.utils.ConvertUtils;
import gss.workshop.testing.utils.OtherUtils;
import gss.workshop.testing.utils.ValidationUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class TrelloTests extends TestBase {

  @Test
  public void trelloWorkflowTest() {
    // 1. Create new board without default list
    String boardName = OtherUtils.randomName();
    Response resBoardCreation = RequestFactory.createBoard(boardName, false);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resBoardCreation, 200);

    // VP. Validate a board is created: Board name and permission level
    BoardCreationRes board =
        ConvertUtils.convertRestResponseToPojo(resBoardCreation, BoardCreationRes.class);
    ValidationUtils.validateStringEqual(boardName, board.getName());
    ValidationUtils.validateStringEqual("private", board.getPrefs().getPermissionLevel());

    // -> Store board Id
    String boardId = board.getId();
    System.out.println(String.format("Board Id of the new Board: %s", boardId));

    // 2. Create a TODO list
        Response resToDoListCreation = RequestFactory.createList(boardId, TODO_List);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resToDoListCreation, 200);

    // VP. Validate a list is created: name of list, closed attribute
    ListCreationRes todoList = ConvertUtils.convertRestResponseToPojo(resToDoListCreation, ListCreationRes.class);
    ValidationUtils.validateStringEqual(TODO_List, todoList.getName());
    ValidationUtils.validateFalseValue(todoList.getClosed());

    // VP. Validate the list was created inside the board: board Id
    ValidationUtils.validateStringEqual(boardId, todoList.getIdBoard());

    // -> Store Todo list id
    String todoListId = todoList.getId();
    System.out.println(String.format("Todo list Id: %s", todoListId));

    // 3. Create a DONE list
    Response resDoneListCreation = RequestFactory.createList(boardId, DONE_List);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resDoneListCreation, 200);

    // VP. Validate a list is created: name of list, "closed" property
    ListCreationRes doneList = ConvertUtils.convertRestResponseToPojo(resDoneListCreation, ListCreationRes.class);
    ValidationUtils.validateStringEqual(DONE_List, doneList.getName());
    ValidationUtils.validateFalseValue(doneList.getClosed());

    // VP. Validate the list was created inside the board: board Id
    ValidationUtils.validateStringEqual(boardId, doneList.getIdBoard());

    // -> Store Done list id
    String doneListId = doneList.getId();
    System.out.println(String.format("Done list Id: %s", doneListId));

    // 4. Create a new Card in TODO list
    String cardName = "Newcard";
    String listId = todoListId;
    Response resNewCardinTodoList = RequestFactory.createCard(cardName, todoListId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resNewCardinTodoList, 200);

    // VP. Validate a card is created: task name, list id, board id
    CardCreationRes newCard = ConvertUtils.convertRestResponseToPojo(resNewCardinTodoList, CardCreationRes.class);
    ValidationUtils.validateStringEqual(cardName, newCard.getName());
    ValidationUtils.validateStringEqual(todoListId, newCard.getIdList());
    ValidationUtils.validateStringEqual(boardId, newCard.getIdBoard());

    // -> Store Done list id
    String cardId = newCard.getId();
    System.out.println(String.format("Card Id: %s", cardId));

    // VP. Validate the card should have no votes or attachments
    ValidationUtils.validateStringEqual(Zero, newCard.getBadges().getVotes().toString());

    // 5. Move the card to DONE list
//    String listId2 = doneListId;
//    Response resMoveCard = RequestFactory.updateCard(cardId, listId2);
//    // VP. Validate status code
//    ValidationUtils.validateStatusCode(resMoveCard, 200);
//
//    // VP. Validate the card should have new list: list id
//    CardCreationRes updateCard = ConvertUtils.convertRestResponseToPojo(resMoveCard, CardUpdateRes.class);
//    ValidationUtils.validateStringEqual(listId2, updateCard.getIdList() );
//
//    // VP. Validate the card should preserve properties: name task, board Id, "closed" property
//    ValidationUtils.validateStringEqual(cardName, updateCard.getName());
//    ValidationUtils.validateStringEqual(boardId, updateCard.getIdBoard());
//    ValidationUtils.validateFalseValue(updateCard.getClosed());

    // 6. Delete board
    Response resDeleteBoard = RequestFactory.deleteBoard(boardId);

    // VP. Validate status code
    ValidationUtils.validateStatusCode(resDeleteBoard, 200);

//    //7. Get board after deleting
//    Response resGetBoardAfterDelete = RequestFactory.getBoardById(boardId);
//    ValidationUtils.validateStatusCode(resGetBoardAfterDelete, 404);
  }
}
