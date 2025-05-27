package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.dto.ListCardDTO;
import com.thomas.gestPro.mapper.BoardMapper;
import com.thomas.gestPro.mapper.ListCardMapper;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import com.thomas.gestPro.repository.UserRepository;
import com.thomas.gestPro.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ListCardRepository listCardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final ListCardMapper listCardMapper;
    private final BoardMapper boardMapper;
    private final UserRepository userRepository;


    /**
     * Constructor with dependency injection for BoardRepository and ListCardRepository.
     *
     * @param boardRepository repository for managing boards
     * @param listCardRepository repository for managing lists of cards
     */
    @Autowired
    public BoardService(BoardRepository boardRepository, ListCardRepository listCardRepository, WorkspaceRepository workspaceRepository, ListCardMapper listCardMapper, BoardMapper boardMapper, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.listCardRepository = listCardRepository;
        this.workspaceRepository = workspaceRepository;
        this.listCardMapper = listCardMapper;
        this.boardMapper = boardMapper;
        this.userRepository = userRepository;
    }


    /**
     * Finds a board by its ID.
     *
     * @param boardId the ID of the board to retrieve
     * @return the board with the given ID
     * @throws ResourceNotFoundException if the board is not found
     */
    public BoardDTO getBoardById(Long boardId) {
        return boardRepository.findById(boardId).map(boardMapper::toDTO).orElseThrow(() -> new ResourceNotFoundException("Board not found"));
    }

    /**
     * Retrieves the set of list cards associated with a specific board.
     *
     * @param id the ID of the board to retrieve the list cards from
     * @return the list of list cards for the specified board
     * @throws RuntimeException if the board is not found
     */
    public List<ListCardDTO> getCardsByBoardId(Long id) {
        return this.getBoardById(id).getListCards();
    }

    /**
     * Retrieves the list of Boards associated with a specific Workspace.
     *
     * @param workspaceId the Workspace identifier
     * @return a list of Boards associated with the Workspace
     * @throws RuntimeException if the Workspace with the specified ID does not exist.
     */

    public List<BoardDTO> getListBoardByWorkspaceId(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new RuntimeException("Workspace not found"));
        return workspace.getBoards().stream().map(boardMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Creates a new Board in a given Workspace.
     *
     * @param workspaceId the identifier of the Workspace where the Board will be added.
     * @param boardInput the Board object to be added
     * @return the newly created Board
     * @throws RuntimeException if the Workspace with the specified ID does not exist.
     */
    @Transactional
    public BoardDTO createBoard(Long workspaceId, BoardDTO boardInput) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        // Charger les membres depuis la base pour éviter les entités détachées
        List<User> attachedMembers = boardInput.getMembers().stream()
                .map(user -> userRepository.findById(user.getId())
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId())))
                .toList();

        Board board = new Board();
        board.setName(boardInput.getName());
        board.setDescription(boardInput.getDescription());
        board.setColor(getRandomColor());
        board.setLastUpdated(new Date());
        board.setCardCount(0);
        board.setMembers(attachedMembers);
        board.setOwnerId(boardInput.getOwnerId());

        board.getWorkspaces().add(workspace);
        attachedMembers.forEach(user -> user.getBoards().add(board));
//        Optional<User> owner = this.userRepository.findById(board.getOwnerId());
//        owner.ifPresent(user -> user.getBoards().add(board));

        workspace.getBoards().add(board);

        boardRepository.save(board);
        return boardMapper.toDTO(board);
    }

    private String getRandomColor() {
        List<String> colors = List.of(
                "red", "blue", "green", "yellow", "purple",
                "orange", "pink", "brown", "mediumaquamarine", "cyan"
        );
        return colors.get(new Random().nextInt(colors.size()));
    }

    /**
     * Updates the name of an existing board.
     *
     * @param id the ID of the board to update
     * @param boardInput the updated board object containing the new name
     * @return the updated board
     * @throws ResourceNotFoundException if the board name is null or empty
     */
    public BoardDTO updateBoard(Long id, BoardDTO boardInput){

        List<User> attachedMembers = boardInput.getMembers()
                .stream()
                .map(userLightDTO -> userRepository.getUserByUsername(userLightDTO.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found: " + userLightDTO.getUsername())))
                .collect(Collectors.toCollection(ArrayList::new));

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        board.setName(boardInput.getName());
        board.setDescription(boardInput.getDescription());
        board.setLastUpdated(new Date());
        board.setStatus(boardInput.getStatus());
        board.setOwnerId(boardInput.getOwnerId());
        board.setMembers(attachedMembers);
        //  attachedMembers.forEach(user -> user.getBoards().add(board));

        boardRepository.save(board);
        return boardMapper.toDTO(board);
    }

    /**
     * Deletes a board by its ID.
     *
     * @param boardId the ID of the board to delete
     */
    public void deleteBoard(Long boardId){
        boardRepository.deleteById(boardId);
    }




}
