package gss.workshop.testing.requests;

import static gss.workshop.testing.utils.RestUtils.addParams;

import gss.workshop.testing.tests.TestBase;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RequestFactory extends TestBase {

  private static final Logger logger = Logger.getLogger(String.valueOf(RequestFactory.class));
  private static HashMap<String, String> params = addParams(Map.of("key", key, "token", token));

  // -------------------Board-------------------

  /**
   * Send request to create a new board
   *
   * @param boardName expected board name
   * @return Response of the request
   */
  public static Response createBoard(String boardName) {
    logger.info("Creating a new board.");
    params.putAll(addParams(Map.of("name", boardName)));
    String requestPath = String.format(prop.getProperty("boardCreationPath"), version);
    Response res =
        RestClient.doPostRequestWithParamsAndNoPayload(
            requestPath,
            params); // it calls a method of RestClient "doPostRequestWithParamsAndNoPayload"  to
    // perform the Post request with specific info was prepared.
    logger.info("Finish board creation.");
    return res;
  }

  /**
   * Send request to create a new board without defaultList
   *
   * @param boardName expected board name
   * @param defaultList a board without/with default list
   * @return Response of the request
   */
  public static Response createBoard(String boardName, boolean defaultList) {
    logger.info("Creating a new board.");
    params.putAll(addParams(Map.of("name", boardName, "defaultLists", false)));
    String requestPath = String.format(prop.getProperty("boardCreationPath"), version);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info("Finish board creation.");
    return res;
  }

  /**
   * Get info of an existing board by its Id
   *
   * @param boardId the Id of an existing board
   * @return Response of the request
   */
  public static Response getBoardById(String boardId) {
    logger.info("Get info by boardID");
    String requestPath = String.format(prop.getProperty("boardIDPath"),version, boardId);
    Response res = RestClient.doGetRequestWithParams(requestPath, params);
    logger.info("Information of the board");
    return res;
  }

  /**
   * Delete an existing board by Id
   *
   * @param boardId the Id of an existing board
   * @return Response of the request
   */
  public static Response deleteBoard(String boardId) {
    logger.info("Delete boardID");
    String requestPath = String.format(prop.getProperty("boardIDPath"),version, boardId);
    Response res = RestClient.doDeleteRequestWithParams(requestPath, params);
    logger.info("The board is deleted success");
    return null;
  }

  // -------------------List-------------------

  /**
   * Create a new list in an existing board
   *
   * @param boardId the board id which to be added more list
   * @param listName name of the new list created
   * @return Response of the request
   */
  public static Response createList(String boardId, String listName) {
    logger.info("Creating a new list");
    params.putAll(addParams(Map.of("name", listName, "idBoard", boardId)));
    String requestPath = String.format(prop.getProperty("listCreationPath"),version);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info("New list is created success");
    return res;
  }

  // -------------------Card-------------------

  /**
   * @param taskName
   * @param listId
   * @return
   */
  public static Response createCard(String taskName, String listId) {
    logger.info("Creating a new card");
    params.putAll(addParams(Map.of("name", taskName, "idList", listId)));
    String requestPath = String.format(prop.getProperty("cardCreationPath"),version);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info("New card is created success");
    return res;

  }

  /**
   * @param cardId
   * @param listId2
   * @return
   */
  public static Response updateCard(String cardId, String listId2) {

    logger.info("Updating a new card");
    params.putAll(addParams(Map.of("List", listId2)));
    String requestPath = String.format(prop.getProperty("updatingCardPath"),version,cardId);
    Response res = RestClient.doPostRequestWithParamsAndNoPayload(requestPath, params);
    logger.info("New card is updated success");
    return res;
  }
}
